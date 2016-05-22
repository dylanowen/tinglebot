package com.dylowen.tinglebot.actors

import akka.actor.{Actor, ActorRef, InvalidActorNameException, Props}
import akka.routing.{DefaultResizer, RoundRobinPool, SmallestMailboxPool}
import com.dylowen.tinglebot.brain.{Brain, LogicalBrain}
import com.dylowen.tinglebot.brain.api.{BInCreateBrain, BOutError, BadRequest}

import scala.concurrent.Promise
import scala.concurrent.duration._
import scala.util.{Failure, Success}
/**
  * This creates new brains
  *
  * @author dylan.owen
  * @since May-2016
  */
object BrainCreator {
}

class BrainCreator[T, V] extends Actor {

  def failBrainExists(promise: Promise[_]): Unit = {
    promise.failure(BadRequest("A brain with that name already exists"))
  }

  def receive = {
    case create: BInCreateBrain =>
      val readName = BrainDispatcher.BRAIN_READ_PREFIX + create.name
      val writeName = BrainDispatcher.BRAIN_WRITE_PREFIX + create.name

      //see if a brain with this name already exists
      BrainDispatcher.getBrain(readName).onComplete({
        case Success(ref) => failBrainExists(create.promise)
        case Failure(e) =>
          //synchronize across the object to ensure we can't ever create mismatched read write actors (hopefully...)
          BrainCreator.synchronized({
            val brain: Brain = new Brain[T, V](4) with LogicalBrain[T, V]
            val readResizer = DefaultResizer(lowerBound = 2, upperBound = 16)

            //even though we're synchronized we can still fail here
            try {
              //TODO investigate dispatchers
              context.system.actorOf(SmallestMailboxPool(4, Some(readResizer)).props(Props(new BrainReader(brain))), readName)
              context.system.actorOf(SmallestMailboxPool(4).props(Props(new BrainWriter(brain))), writeName)
            } catch {
              case e: InvalidActorNameException => failBrainExists(create.promise)
              case e: Exception => create.promise.failure(e)
            }
          })
      })
    case _ => println("Unknown message")
  }
}

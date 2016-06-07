package com.dylowen.tinglebot.actors

import akka.actor.{Actor, InvalidActorNameException, Props}
import akka.routing.{DefaultResizer, SmallestMailboxPool}
import com.dylowen.tinglebot.BrainDispatcher
import com.dylowen.tinglebot.brain.Brain
import com.dylowen.tinglebot.brain.api.{BInCreateBrain, BOutCreateBrain, BadRequest}

import scala.util.{Failure, Success}
/**
  * This creates new brains
  *
  * @author dylan.owen
  * @since May-2016
  */
object BrainCreator {
  val BRAIN_EXISTS_ERROR: String = "A brain with that name already exists"
  val BAD_BRAIN_NAME_ERROR: String = "Bad brain name"

  val VALID_NAME = """[a-z][a-zA-Z0-9]*""".r
}

class BrainCreator extends Actor {
  import context.dispatcher

  def receive = {
    case create: BInCreateBrain =>
      //validate the brain name
      if(!BrainCreator.VALID_NAME.pattern.matcher(create.name).matches()) {
        create.promise.failure(BadRequest(BrainCreator.BAD_BRAIN_NAME_ERROR))
      }
      else {
        val readName = BrainDispatcher.readBrainName(create.name)
        val writeName = BrainDispatcher.writeBrainName(create.name)

        //see if a brain with this name already exists
        BrainDispatcher.getBrain(readName)(context.system).onComplete({
          case Success(_) => create.promise.failure(BadRequest(BrainCreator.BRAIN_EXISTS_ERROR))
          case Failure(_) =>
            //synchronize across the object to ensure we can't ever create mismatched read write actors (hopefully...)
            BrainCreator.synchronized({
              //even though we're synchronized we can still fail here so look for an invalid actor name exception
              try {
                val brain = new Brain[String, String](4, null)// with LogicalBrain[T, V]
                val readResizer = DefaultResizer(lowerBound = 2, upperBound = 16)

                //TODO investigate dispatchers
                context.system.actorOf(SmallestMailboxPool(2, Some(readResizer)).props(Props(new BrainReader(brain))), readName)
                context.system.actorOf(SmallestMailboxPool(4).props(Props(new BrainWriter(brain))), writeName)

                create.promise.success(BOutCreateBrain(create.name))
              } catch {
                case _: InvalidActorNameException => create.promise.failure(BadRequest(BrainCreator.BRAIN_EXISTS_ERROR))
                case e: Exception => create.promise.failure(e)
              }
            })
        })
      }
    case _ => println("Unknown message")
  }
}

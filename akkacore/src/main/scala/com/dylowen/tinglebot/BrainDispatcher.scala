package com.dylowen.tinglebot

import akka.actor.{ActorContext, ActorRef, ActorSystem}
import akka.event.Logging
import com.dylowen.tinglebot.brain.api._

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */

object BrainDispatcher {
  val BRAIN_READ_PREFIX = "brain_read_"
  val BRAIN_WRITE_PREFIX = "brain_write_"

  def readBrainName(name: String) = BRAIN_READ_PREFIX + name
  def writeBrainName(name: String) = BRAIN_WRITE_PREFIX + name

  protected[tinglebot] def getBrain(fullName: String)(implicit system: ActorSystem): Future[ActorRef] = system.actorSelection("/user/" + fullName).resolveOne(10.seconds)
}

class BrainDispatcher(implicit system: ActorSystem) {
  //val log = Logging(system, this)
  implicit val executionContext = system.dispatcher
  //create a routing actor in our props that routes the data

  //create a router that holds one brain and then we create a pool of reads and writes that are all passed the brain instance
  //use the actor system to lookup this router that we will use to operate on the brain

  def getBrainPool(name: String, poolType: ActionType): Future[ActorRef] = {
    val fullName = poolType match {
      case Read => BrainDispatcher.readBrainName(name)
      case Write => BrainDispatcher.writeBrainName(name)
    }

    BrainDispatcher.getBrain(fullName)
  }

  //send a message to the brain
  def messageBrain(action: BInBrain): Unit = {
    //find the brain and return it if it exists, otherwise return an exception in our action promise
    getBrainPool(action.name, action.actionType).onComplete({
      case Success(brainPool) => brainPool ! action
      case Failure(e) => action.promise.failure(BrainNotFound("The brain " + action.name + " couldn't be found"))
    })
  }
}

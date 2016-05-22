package com.dylowen.tinglebot.actors

import akka.actor.{Actor, ActorContext, ActorRef, Props}
import akka.event.Logging
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.dylowen.tinglebot.brain.api._
import com.dylowen.tinglebot.brain.{Brain, LogicalBrain}

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

  def getBrain(fullName: String)(implicit context: ActorContext): Future[ActorRef] = context.system.actorSelection("/user/" + fullName).resolveOne(10.seconds)
}

class BrainDispatcher extends Actor {
  import context.dispatcher

  val log = Logging(context.system, this)
  //create a routing actor in our props that routes the data

  //val brains: mutable.Map[String, ActorRef] = mutable.Map()

  //look up actor by id since it's inherent in the system. so create brain can be easier
  //val createBrainActor = context.actorOf(Props(new CreateBrain(brains)))

  //create a router that holds one brain and then we create a pool of reads and writes that are all passed the brain instance
  //use the actor system to lookup this router that we will use to operate on the brain

  //find the brain and return it if it exists, otherwise return an exception in our action promise
  def findBrain(action: BInBrain, onSuccess: ActorRef => Unit): Unit = {
    val fullName = action match {
      case _: BInReadBrain => BrainDispatcher.readBrainName(action.name)
      case _: BInWriteBrain => BrainDispatcher.writeBrainName(action.name)
    }

    BrainDispatcher.getBrain(fullName).onComplete({
      case Success(brainRef) => onSuccess(brainRef)
      case Failure(e) => action.promise.failure(BrainNotFound("The brain " + action.name + " couldn't be found"))
    })
  }

  //send a message to the brain
  def messageBrain(action: BInBrain) = {
    //search for a brain based on the action and send our action to the brain pool
    findBrain(action, (brainPool) => {
      log.debug("messaging pool: " + brainPool.path)
      //println("messaging pool: " + brainPool.path)
      brainPool ! action
    })
  }

  def receive = {
    case action: BInBrain => messageBrain(action)
    case message => log.error("Unknown message")
  }
}

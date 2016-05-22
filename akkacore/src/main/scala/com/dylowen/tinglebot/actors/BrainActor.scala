package com.dylowen.tinglebot.actors

import akka.actor.Actor
import akka.event.Logging
import com.dylowen.tinglebot.brain.api.{BInBrain, BOutSuccess, InternalError}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
object BrainActor {
  val SUCCESS: BOutSuccess = new BOutSuccess()
}

abstract class BrainActor extends Actor {
  val log = Logging(context.system, this)

  def unexpectedMessage(message: Any) = message match {
    case bInBrain: BInBrain =>
      val errorMessage = "The wrong message type was dispatched here: " + bInBrain

      //log the error and return it so at least our promise is fulfilled
      log.error(errorMessage)
      bInBrain.promise.failure(InternalError(errorMessage))
    case unknown => log.error("Unknown Message: " + unknown)
  }
}

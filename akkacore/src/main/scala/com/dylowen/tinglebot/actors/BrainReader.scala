package com.dylowen.tinglebot.actors

import akka.actor.Actor
import com.dylowen.tinglebot.brain.Brain
import com.dylowen.tinglebot.brain.api.{BInBrain, BInReadBrain, BOutTemp, InternalError}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
class BrainReader[T >: Null, V](brain: Brain[T, V]) extends BrainActor {
  def receive = {
    case message: BInReadBrain =>
      println("received read message")

      //Thread.sleep(100)
      message.promise.success(BOutTemp())

    //handle anything we don't understand
    case message => unexpectedMessage(message)
  }
}

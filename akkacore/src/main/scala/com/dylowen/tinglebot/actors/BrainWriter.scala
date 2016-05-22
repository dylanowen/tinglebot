package com.dylowen.tinglebot.actors

import akka.actor.Actor
import com.dylowen.tinglebot.brain.Brain
import com.dylowen.tinglebot.brain.api.{BInBrain, BInWriteBrain, BOutTemp, InternalError}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
class BrainWriter[T, V](brain: Brain[T, V]) extends BrainActor {
  def receive = {
    case message: BInWriteBrain =>
      Thread.sleep(1)
      message.promise.success(BOutTemp())

    //handle anything we don't understand
    case message => receiveBadMessage(message)
  }
}

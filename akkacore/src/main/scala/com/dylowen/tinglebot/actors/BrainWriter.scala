package com.dylowen.tinglebot.actors

import akka.actor.Actor
import com.dylowen.tinglebot.brain.Brain
import com.dylowen.tinglebot.brain.api._

import scala.util.Success

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
class BrainWriter[T >: Null, V](brain: Brain[T, V]) extends BrainActor {
  def receive = {
    case message: BInTrainBrain[T] =>
      //train our brain
      brain.train(message.sentence)

      //println(self.path.name, brain.gramSize)

      //println(brain.toString)

      message.promise.success(BrainActor.SUCCESS)
    //handle anything we don't understand
    case message => unexpectedMessage(message)
  }
}

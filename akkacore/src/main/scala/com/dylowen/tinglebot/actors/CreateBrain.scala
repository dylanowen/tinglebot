package com.dylowen.tinglebot.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.Router
import com.dylowen.tinglebot.api.{BInCreateBrain, BOutCreateBrain, BOutError}

import scala.collection.mutable

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
object CreateBrain {
  def create(name: String): Unit = {
    val router = Router
  }
}

class CreateBrain(val brains: mutable.Map[String, ActorRef]) extends Actor {
  def receive = {
    case BInCreateBrain(name, callback) =>
      if (brains.contains(name)) {
        callback(BOutError("duplicate name"))
      }
      else {
        val brain = context.actorOf(Props[Brain], name = "brain_" + name)

        brains.put(name, brain)

        callback(BOutCreateBrain(name))
      }
    case _ => System.out.println("Unknown message")
  }
}

package com.dylowen.tinglebot

import akka.actor.{Actor, ActorRef, Props}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
object BrainCollection {
  case class Create(name: String)
  case class Get(name: String)

  case class BrainDefinition(name: String)
  case class Error(message: String)

  val VALID_NAME = """[a-z][a-zA-Z0-9]*""".r
}

class BrainCollection extends Actor {

  var brains = Map[String, ActorRef]()

  def receive = {
    case BrainCollection.Create(name) => {
      if (brains.contains(name)) {
        sender() ! BrainCollection.Error("That brain already exists")
      }
      else if (!BrainCollection.VALID_NAME.pattern.matcher(name).matches()) {
        sender() ! BrainCollection.Error(f"'$name' is a bad brain name")
      }
      else {
        brains += (name -> context.actorOf(Props[Brain], name))

        sender() ! BrainCollection.BrainDefinition("Brain Created")
      }
    }
    case BrainCollection.Get(name) => {
      if (!brains.contains(name)) {
        sender() ! BrainCollection.Error("Could not find brain")
      }
      else {
        sender() ! BrainCollection.BrainDefinition(name)
      }
    }
  }
}

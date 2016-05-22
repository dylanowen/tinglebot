package com.dylowen.tinglebot.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.dylowen.tinglebot.api.{BrainRead, BrainWrite}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
class BrainRouter extends Actor {


  val writeRouter:

  def receive = {
    case read: BrainRead => readRouter ! read
    case write: BrainWrite =>
  }
}

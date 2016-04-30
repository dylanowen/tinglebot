package com.dylowen.tinglebot.brain

import akka.actor.Actor

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */
class SentenceToNGrams[T] extends Actor {
  case class Sentence(sentence: List[T])

  def receive = {
    case Sentence(sentence) => {
      for (word <- sentence) {
        println(self.path.name, word)
      }
    }
  }
}

package com.dylowen.tinglebot.brain

import scala.concurrent.Promise

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
package object api {
  abstract class BIn {
    def promise: Promise[BOut]
  }

  abstract class BOut

  abstract class BInBrain extends BIn {
    def name: String
  }

  abstract class BrainRead extends BInBrain
  abstract class BrainWrite extends BInBrain

  class BInCreateBrain(val name: String, val promise: Promise[BOut]) extends BInBrain
  class BOutCreateBrain(val name: String) extends BOut

  class BInUpdateBrain(val name: String, sentence: List[String], val promise: Promise[BOut]) extends BInBrain

  class BInReadBrain(val name: String, val promise: Promise[BOut]) extends BInBrain

  case class BadRequest(message: String) extends Exception(message)

  case class BrainNotFound(message: String) extends Exception(message)
}
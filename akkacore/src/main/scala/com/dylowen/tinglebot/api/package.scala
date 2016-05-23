package com.dylowen.tinglebot.brain

import scala.concurrent.Promise

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
package object api {
  /***************************************
    Incoming Messages
    ****************************************/

  abstract class BIn {
    //any request coming into our system will be fulfilled with this promise
    def promise: Promise[BOut]
  }

  /***************************************
    Specific Brain Interaction
   ****************************************/

  //any request that's interacting with a specific brain
  abstract class BInBrain extends BIn {
    def name: String
  }

  case class BInCreateBrain(name: String, promise: Promise[BOut]) extends BInBrain

  //separate out the message types we'll be sending to brains
  abstract class BInReadBrain extends BInBrain

  abstract class BInWriteBrain extends BInBrain

  case class BInTrainBrain(name: String, sentence: List[String], promise: Promise[BOut]) extends BInWriteBrain

  /***************************************
    Outgoing Messages
    ****************************************/

  abstract class BOut

  class BOutSuccess extends BOut

  case class BOutCreateBrain(name: String) extends BOut

  case class BOutTemp() extends BOut

  /***************************************
    Exceptions
    ****************************************/

  case class InternalError(message: String) extends Exception(message)
  case class BadRequest(message: String) extends Exception(message)
  case class BrainNotFound(message: String) extends Exception(message)
}
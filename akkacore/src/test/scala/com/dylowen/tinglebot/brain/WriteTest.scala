package com.dylowen.tinglebot.brain

import akka.actor.{ActorSystem, Props}
import com.dylowen.tinglebot.actors.{BrainCreator, BrainDispatcher}
import com.dylowen.tinglebot.brain.api.{BInCreateBrain, BInTrainBrain, BOut}
import org.junit.Test

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
class WriteTest {

  val system = ActorSystem("TestWriteSystem")
  implicit val executionContext = system.dispatcher

  @Test
  def testThroughput(): Unit = {
    val brainName = "testBrain"
    val brainCreator = system.actorOf(Props[BrainCreator])
    val brainDispatcher = system.actorOf(Props[BrainDispatcher])
    //implicit val executor = createBrain.

    val createdBrain: Promise[BOut] = Promise[BOut]()
    brainCreator ! BInCreateBrain(brainName, createdBrain)

    //wait till our brain is created
    Await.result(createdBrain.future, 10.seconds)

    val updateList: mutable.ListBuffer[Future[BOut]] = mutable.ListBuffer()

    for (i <- 1 to 1000000) {
      val updatePromise = Promise[BOut]()
      brainDispatcher ! BInTrainBrain(brainName, List("a", "b", "c", "d", "e"), updatePromise)

      updateList += updatePromise.future
    }

    val allUpdatePromise = Promise[List[BOut]]()
    Future.sequence(updateList.toList).foreach(allUpdatePromise.trySuccess)

    Await.result(allUpdatePromise.future, 10.seconds)

    println("done")
  }
}

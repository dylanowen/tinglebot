package com.dylowen.tinglebot.brain.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import com.dylowen.tinglebot.actors.BrainCreator
import com.dylowen.tinglebot.brain.api.{BInCreateBrain, BOut}
import org.junit.{Assert, Test}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success, Try}

/**
  * Tests {@link BrainCreator}
  *
  * @author dylan.owen
  * @since May-2016
  */
object BrainCreatorTest {
  val TEST_BRAIN_NAME = "testBrain"
}

class BrainCreatorTest {
  val system = ActorSystem("TestWriteSystem")
  implicit val executionContext = system.dispatcher

  val brainCreator = system.actorOf(Props[BrainCreator])
  val brainCreatorAlternate = system.actorOf(Props[BrainCreator])

  @Test
  def testNames(): Unit = {
    Assert.assertFalse(checkSuccess(Await.result(createBrain(""), 10.seconds)))
    Assert.assertFalse(checkSuccess(Await.result(createBrain("AbadName"), 10.seconds)))
    Assert.assertFalse(checkSuccess(Await.result(createBrain("0badName"), 10.seconds)))
    Assert.assertFalse(checkSuccess(Await.result(createBrain("%badname"), 10.seconds)))
    Assert.assertFalse(checkSuccess(Await.result(createBrain("badname*"), 10.seconds)))
    Assert.assertFalse(checkSuccess(Await.result(createBrain("234"), 10.seconds)))
    Assert.assertFalse(checkSuccess(Await.result(createBrain("@#"), 10.seconds)))
    Assert.assertTrue(checkSuccess(Await.result(createBrain("goodCamelCaseName"), 10.seconds)))
    Assert.assertTrue(checkSuccess(Await.result(createBrain("goodNameWithNumbers12153Numbers235numbers"), 10.seconds)))
  }

  @Test
  def testDuplicates(): Unit = {
    val goodResult = Await.result(createBrain(BrainCreatorTest.TEST_BRAIN_NAME), 10.seconds)
    Assert.assertTrue(checkSuccess(goodResult))

    val badResult = Await.result(createBrain(BrainCreatorTest.TEST_BRAIN_NAME), 10.seconds)
    //make sure we got an error with the duplicate insert
    Assert.assertFalse(checkSuccess(badResult))
  }

  @Test
  def testManyDuplicates(): Unit = {
    val createFuturesBuffer: ListBuffer[Future[Try[BOut]]] = ListBuffer()

    for (i <- 0 until 10000 by 2) {
      val name = BrainCreatorTest.TEST_BRAIN_NAME + i

      createFuturesBuffer += createBrain(name, brainCreator)
      createFuturesBuffer += createBrain(name, brainCreatorAlternate)
    }
    val createFutures = createFuturesBuffer.toList

    val result = Await.result(Future.sequence(createFutures), 60.seconds)

    for (i <- result.indices by 2) {
      var success = 0

      if (checkSuccess(result(i))) {
        success += 1
      }
      if (checkSuccess(result(i + 1))) {
        success += 1
      }

      //make sure we found only 1 success
      Assert.assertEquals(1, success)
    }
  }

  /**
    * create a brain
    *
    * @param name the name of the brain
    * @return a future that catches failures and wraps them in a Failure
    */
  private def createBrain(name: String, creator: ActorRef = this.brainCreator): Future[Try[BOut]] = {
    val createdBrain: Promise[BOut] = Promise[BOut]()
    creator ! BInCreateBrain(name, createdBrain)

    //make sure we don't throw exceptions for the results and we just wrap them in a failure (easier for testing)
    createdBrain.future.map(Success(_)).recover({case x => Failure(x)})
  }

  private def checkSuccess(result: Try[BOut]): Boolean = result match {
    case Success(_) => true
    case Failure(_) => false
  }
}

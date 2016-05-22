package com.dylowen.tinglebot.brain

import scala.annotation.tailrec
import scala.collection.concurrent.TrieMap
import scala.util.Random

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
object Brain {
  val MIN_GRAM_SIZE: Integer = 2
}

class Brain[T >: Null, V](var gramSize: Integer) {
  protected var dictionary: TrieMap[NGram[T], WeightedSet[T]] = TrieMap()

  //val dictionary: scala.collection.mutable.Map[NGram[T], WeightedSetJava[T]] = new mutable.HashMap[]()

  @transient
  private val rand: Random = new Random()
  //@transient
  //private var input: List[T] = Nil

  def train(sentence: List[T]): Boolean = {
    if (sentence.size > Brain.MIN_GRAM_SIZE) {
      //loop over every spot we could have an nGram
      for (i <- 0 until sentence.size - Brain.MIN_GRAM_SIZE) {
        //only create nGrams for the space we have left in the sentence
        for (j <- Range.inclusive(Brain.MIN_GRAM_SIZE, Integer.min(sentence.size - i, this.gramSize))) {
          val endIndex = i + j

          val nGram = new NGram(sentence.slice(i, endIndex))
          val word: T = if (endIndex >= sentence.size) null else sentence(endIndex)

          train(nGram, word)
        }
      }

      true
    }
    else {
      false
    }
  }

  def train(gram: NGram[T], word: T): Unit = {
    val set = this.dictionary.getOrElseUpdate(gram, {new WeightedSet[T]()})

    set.add(word)
  }

  /*
  def feed(word: T): Unit = {
    if (this.input.size >= this.gramSize) {
      var subList: List[T] = this.input
      for (i <- 0 to this.gramSize - Brain.MIN_GRAM_SIZE) {
        val nGram = new NGram[T](subList)

        val set = this.dictionary.get(nGram) match {
          case Some(v) => v
          case None =>
            val newSet = new WeightedSet[T]()
            this.dictionary += (nGram -> newSet)

            newSet
        }

        set.add(word)

        subList = subList.tail
      }

      this.input = this.input.tail
    }

    this.input :+= word
  }
  */

  //def compress(): Unit

  /*
  def getSentenceWords: List[T] = {
    val start: List[T] = this.getRandomWords

    continueSentence(start)
  }
  */

  /*
  @tailrec
  private def continueSentence(sentenceHead: List[T]): List[T] = {
    val nextWord: Option[T] = getNextWord(sentenceHead)

    if (nextWord.exists(v => !shouldContinueSentence(v, sentenceHead))) {
      return sentenceHead
    }

    continueSentence(sentenceHead :+ nextWord.get)
  }
  */

  override def toString: String = {
    val builder = new StringBuilder()

    for (entry <- this.dictionary) {
      builder.append(entry._1.toString + " -> " + entry._2.toString + "\n")
    }

    builder.toString
  }

  //protected def shouldContinueSentence(word: T, sentence: List[T]): Boolean

  protected def getNextWord(input: List[T]): Option[T] = {
    val startIndex = if (this.gramSize > input.size) 0 else input.size - this.gramSize
    var operatingList = input.slice(startIndex, input.size)

    var set: Option[WeightedSet[T]] = None
    do {
      val operatingGram: NGram[T] = new NGram[T](operatingList)
      set = this.dictionary.get(operatingGram)

      //System.out.println(operatingGram.toString() + " : [" + set.map(_.toString).getOrElse("") + "]")
      //System.out.println(operatingList.size + " : [" + set.map(_.size).getOrElse("") + "]\n")

      operatingList = operatingList.tail
      //loop if we get a null set or the set size is uninteresting and we still have an operating list
    } while (operatingList.size > 2 /*Brain.MIN_GRAM_SIZE*/ - 1 && (set.isEmpty || set.exists(v => v.size <= 1)))

    set.map(v => v.get)
  }

  //def concatSentence(word: List[T]): V

  def stateCount = this.dictionary.size

  private var cachedNGramArray: Array[NGram[T]] = null
  private def getRandomWords: List[T] = {
    if (this.cachedNGramArray == null || this.cachedNGramArray.length != this.dictionary.size) {
      this.cachedNGramArray = this.dictionary.keySet.toArray
    }

    while (true) {
      val startGram: NGram[T] = this.cachedNGramArray(this.rand.nextInt(this.cachedNGramArray.length))

      if (startGram.size >= this.gramSize) {
        return startGram.words
      }
    }

    throw new AssertionError("You should never get here")
  }
}

package com.dylowen.tinglebot.brain

import java.io.ObjectInputStream
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.concurrent.TrieMap
import scala.util.Random

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
class WeightedSet[T](var map: TrieMap[T, AtomicInteger] = TrieMap[T, AtomicInteger]()) extends Serializable {
  //private val map: TObjectIntHashMap[T] = new TObjectIntHashMap[T]()
  private val total: AtomicInteger = new AtomicInteger(0)

  @transient
  private var rand: Random = new Random()

  def add(obj: T): Unit = add(obj, 1)

  def add(obj: T, inc: Integer): Unit = {
    val weight = map.getOrElseUpdate(obj, {new AtomicInteger(0)})

    weight.addAndGet(inc)
    this.total.addAndGet(inc)
  }

  def get: T = {
    var offset: Integer = 0
    val randValue: Integer = this.rand.nextInt(this.total.get())

    this.map.foreach(entry => {
      offset += entry._2.get()

      if (randValue < offset) {
        return entry._1
      }
    })

    throw new AssertionError("How did you get here? offset = " + offset + " randValue = " + randValue)
  }

  def foreach(f: ((T, AtomicInteger)) => Unit): Unit = this.map.foreach(f)

  def size = map.size

  override def toString: String = {
    val builder = StringBuilder.newBuilder

    builder.append(map.head._1)
    builder.append(':')
    builder.append(map.head._2)

    for ((key, weight) <- map.tail) {
      builder.append(" ")
      builder.append(key)
      builder.append(':')
      builder.append(weight)
    }

    builder.toString()
  }

  private def readObject(stream: ObjectInputStream): Unit = {
    stream.defaultReadObject()
    this.rand = new Random()
  }
}

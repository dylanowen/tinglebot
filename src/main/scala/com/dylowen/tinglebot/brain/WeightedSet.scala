package com.dylowen.tinglebot.brain

import java.io.ObjectInputStream

import scala.util.Random

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
class WeightedSet[T](var map: Map[T, Integer] = Map[T, Integer]()) extends Serializable {
  //private val map: TObjectIntHashMap[T] = new TObjectIntHashMap[T]()
  private var total: Integer = 0

  @transient
  private var rand: Random = new Random()

  def add(obj: T): Unit = add(obj, 1)

  def add(obj: T, inc: Integer): Unit = {
    val weight: Integer = map.getOrElse(obj, 0)

    map += (obj -> (weight + inc))
    //this.map.adjustOrPutValue(obj, inc, inc)

    this.total += inc
  }

  def get: T = {
    var offset: Integer = 0
    val randValue: Integer = this.rand.nextInt(this.total)

    this.map.foreach(entry => {
      offset += entry._2

      if (randValue < offset) {
        return entry._1
      }
    })

    throw new AssertionError("How did you get here? offset = " + offset + " randValue = " + randValue)
  }

  def foreach(f: ((T, Integer)) => Unit): Unit = this.map.foreach(f)

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

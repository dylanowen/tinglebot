package com.dylowen.tinglebot.brain

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
class NGram[T](private val _words: List[T]) extends Serializable {
  def words = this._words

  def size = this._words.size

  override def toString: String = {
    val builder = StringBuilder.newBuilder

    builder.append(this._words.head)

    this._words.tail.foreach(word => {
      builder.append(" ")
      builder.append(word)
    })

    builder.toString()
  }

  override def hashCode = this._words.hashCode

  override def equals(that: Any): Boolean = that match {
    case that: NGram[T] => this._words.equals(that.words)
    case _ => false
  }
}

package com.dylowen.tinglebot.brain

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  *
trait DumbBrain[T, V] extends Brain[T, V] {

  //dumbly strip out all the weighted sets we ignore
  def compress(): Unit = {
    //loop over all the nGrams larger than the minimum where
    for ((nGram, set) <- this.dictionary if nGram.size > Brain.MIN_GRAM_SIZE && set.size == 0) {
      this.dictionary -= nGram
    }
  }
}
*/
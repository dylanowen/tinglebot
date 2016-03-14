package com.dylowen.tinglebot.brain

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
trait LogicalBrain[T, V] extends Brain[T, V] {
  override def compress(): Unit = {
    //loop over all the nGrams larger than the minimum size
    for ((nGram, set) <- this.dictionary if nGram.size > MIN_GRAM_SIZE) {
      //add the nGram we're stripping out to the min nGrams with some weight
      val minSet = getMinGramSet(nGram.words)
      val weight = nGram.size - 1
      set.foreach(tuple =>
        minSet.add(tuple._1, tuple._2 * weight)
      )

      this.dictionary -= nGram
    }

    this.gramSize = MIN_GRAM_SIZE
  }

  private def getMinGramSet(words: List[T]): WeightedSet[T] = {
    val searchNGram = new NGram[T](words.slice(words.size - MIN_GRAM_SIZE, words.size))
    val minNGram = this.dictionary.get(searchNGram)

    if (minNGram.isDefined) {
      return minNGram.get
    }

    throw new AssertionError("How did you get here? words = " + words.toString)
  }
}

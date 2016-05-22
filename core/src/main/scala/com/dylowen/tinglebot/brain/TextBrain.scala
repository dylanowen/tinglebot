package com.dylowen.tinglebot.brain

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
abstract class TextBrain(gramSize: Integer) extends Brain[String, String](gramSize) {

  @transient
  private val MAX_WORDS_IN_SENTENCE: Integer = 50

  //if we can't continue, inject a period
  override def getNextWord(input: List[String]): Option[String] = Option(super.getNextWord(input).getOrElse("."))

  override def concatSentence(words: List[String]): String = words.foldLeft(new StringBuilder())((builder, word) =>
    builder.append(GenericWordType.getSpacedWord(word))
  ).toString()

  override def shouldContinueSentence(word: String, sentence: List[String]): Boolean = {
    GenericWordType.getType(word) != GenericWordType.END_SENTENCE && sentence.size < MAX_WORDS_IN_SENTENCE
  }
}

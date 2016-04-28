package com.dylowen.tinglebot.train

import java.util.stream.Collectors

import com.dylowen.tinglebot.brain.Brain

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Mar-2016
  */
/*
object Trainer {
  protected val GRAM_SIZE = 4

  def wordsFromLine(line: String): List[String] = {
    val dirtyWords: Array[String] = line.split(" ").filter(word => !word.startsWith("http") || word.length == 0)
    val cleanWords: List[String] = new List[]

    for (dirtyWord <- dirtyWords) {
      val sb: StringBuilder = new StringBuilder
      val emoji = '(' == dirtyWord.charAt(0) && ')' == dirtyWord.charAt(dirtyWord.length - 1)

      if (emoji) {
        sb.append("(")
      }

      var foundEnd = false
      //var
    }
  / *
      Arrays.asList(line.split(" ")).parallelStream.filter(word -> !(word.startsWith("http") || word.length() == 0)).collect(Collectors.toList)
    val cleanWords: util.List[String] = new util.ArrayList[String]
    import scala.collection.JavaConversions._
    for (dirtyWord <- dirtyWords) {
      val sb: StringBuilder = new StringBuilder
      val emoji: Boolean = '(' == dirtyWord.charAt(0) && ')' == dirtyWord.charAt(dirtyWord.length - 1)
      if (emoji) {
        sb.append("(")
      }
      var foundEnd: Boolean = false
      var i: Int = 0
      while (i < dirtyWord.length - 1) {
        {
          val c: Char = dirtyWord.charAt(i)
          if (isCharValid(c)) {
            sb.append(c)
            if (isCharEnd(c)) {
              foundEnd = true
            }
          }
        }
        ({
          i += 1; i - 1
        })
      }
      var nextWord: String = null
      val c: Char = dirtyWord.charAt(dirtyWord.length - 1)
      if (isCharValid(c)) {
        if ((isCharEnd(c) && foundEnd) || (!isCharEnd(c) && !isCharPunctuation(c))) {
          sb.append(c)
        }
        else {
          nextWord = Character.toString(c)
        }
      }
      if (emoji) {
        sb.append(")")
      }
      if (sb.length > 0) {
        cleanWords.add(sb.toString)
      }
      if (nextWord != null) {
        cleanWords.add(nextWord)
      }
    }
    return cleanWords
  }

  private def isCharEnd(c: Char): Boolean = {
    '!' == c || '?' == c || '.' == c
  }

  private def isCharPunctuation(c: Char): Boolean = {
    ',' == c || ';' == c
  }

  private def isCharValid(c: Char): Boolean = {
    case '\'' | ':' | '$'| '#' => true
    case _ => c.isLetterOrDigit || isCharEnd(c) || isCharPunctuation(c)
  }

  / *
  private def isCharValid(c: Char): Boolean = {
return Character.isAlphabetic(c) || Character.isDigit(c) || '\'' == c || ':' == c || '$' == c || '#' == c || isCharEnd(c) || isCharPunctuation(c)
}

   * /
}

abstract class Trainer[T <: Brain] {
  def train: T
}
*/
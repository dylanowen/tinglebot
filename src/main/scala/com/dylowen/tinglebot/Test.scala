package com.dylowen.tinglebot

import java.util.Arrays
import java.util.stream.Collectors

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.{RandomPool, RandomRoutingLogic, RoundRobinPool}

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */
object Test {

  val system = ActorSystem("ParseSystem")

  def main(args: Array[String]): Unit = {
    val sentences = List("Hello there test", "Another Sentence.")

    val actor = system.actorOf(RandomPool(2).props(Props[SentenceParser]))

    for (sentence <- sentences) {
      actor ! sentence
    }
  }

  class SentenceToNGrams extends Actor {
    def receive = {
      case words: List[String] => {
        for (word <- words) {
          println(self.path.name, word)
        }
      }
      case _ => System.out.println("Unknown message")
    }
  }

  class SentenceParser extends Actor {

    val next = system.actorOf(Props[SentenceToNGrams])

    private def parse(sentence: String): List[String] = {
      val dirtyWords: Array[String] = sentence.split(' ').filter(word => !(word.startsWith("http") || word.length() == 0))
      var cleanWords: List[String] = Nil

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

          i += 1
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

        if (sb.nonEmpty) {
          cleanWords :+= sb.toString
        }
        if (nextWord != null) {
          cleanWords :+= nextWord
        }
      }

      cleanWords
    }

    private def isCharEnd(c: Char): Boolean = '!' == c || '?' == c || '.' == c

    private def isCharPunctuation(c: Char): Boolean = ',' == c || ';' == c

    private def isCharValid(c: Char): Boolean = {
      Character.isAlphabetic(c) ||
        Character.isDigit(c) ||
        '\'' == c || ':' == c || '$' == c || '#' == c ||
        isCharEnd(c) ||
        isCharPunctuation(c)
    }

    def receive = {
      case value: String => {
        val wordList = parse(value)

        next ! wordList
      }
      case _ => System.out.println("Unknown message")
    }
  }

}

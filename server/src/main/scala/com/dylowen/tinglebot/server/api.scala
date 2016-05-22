package com.dylowen.tinglebot.server

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since May-2016
  */
package object api {
  private[server] final case class Error(message: String)

  private[server] final case class InCreateBrain(name: String)
  private[server] final case class BrainDefinition(name: String)
}

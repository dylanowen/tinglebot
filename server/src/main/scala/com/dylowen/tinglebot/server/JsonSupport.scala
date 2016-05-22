package com.dylowen.tinglebot.server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.dylowen.tinglebot.server.api.{Error, InCreateBrain, OutCreateBrain, WTF}
import spray.json.DefaultJsonProtocol

import scala.language.implicitConversions

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {



}
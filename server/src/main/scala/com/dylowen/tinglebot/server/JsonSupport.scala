package com.dylowen.tinglebot.server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.dylowen.tinglebot.server.api.{InCreateBrain, OutCreateBrain}
import spray.json.DefaultJsonProtocol

import scala.language.implicitConversions

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val createInBrainFormat = jsonFormat1(InCreateBrain)
  implicit val createOutBrainFormat = jsonFormat1(OutCreateBrain)
}
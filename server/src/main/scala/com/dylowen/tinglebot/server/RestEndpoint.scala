package com.dylowen.tinglebot.server

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.dylowen.tinglebot.server.api._

import scala.concurrent.ExecutionContext

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */
class RestEndpoint(implicit val system: ActorSystem, implicit val ec: ExecutionContext) extends JsonSupport with Directives {
  implicit val errorFormat = jsonFormat1(Error)

  implicit val createInBrainFormat = jsonFormat1(InCreateBrain)
  implicit val createOutBrainFormat = jsonFormat1(BrainDefinition)


  implicit val timeout: Timeout = Timeout(60, TimeUnit.SECONDS)

  /*
  val brains: ActorRef = system.actorOf(Props[BrainCollection], "brains")

  def route: Route = pathPrefix("v1") {
    pathPrefix("brains") {
      pathEndOrSingleSlash {
        get {
          complete("{[]}")
        } ~
          post {
            entity(as[InCreateBrain]) {
              inCreateBrain => {
                //TODO this is dumb
                complete({
                  (brains ? BrainCollection.Create(inCreateBrain.name)).map({
                    case BrainCollection.BrainDefinition(name) => ToResponseMarshallable(Created -> BrainDefinition(name))
                    case BrainCollection.Error(message) => ToResponseMarshallable(BadRequest -> Error(message))
                    case _ => ToResponseMarshallable(InternalServerError -> Error("Internal Server Error"))
                  })
                })
              }
            }
          }
      } ~
        path(Segment) { name => {
          get {
            complete({
              (brains ? BrainCollection.Get(name)).map({
                case BrainCollection.BrainDefinition(name) => ToResponseMarshallable(Created -> BrainDefinition(name))
                case BrainCollection.Error(message) => ToResponseMarshallable(NotFound -> Error(message))
                case _ => ToResponseMarshallable(InternalServerError -> Error("Internal Server Error"))
              })
            })
          }
        }
        } ~ complete(ToResponseMarshallable(NotFound -> Error("Not found")))
    }
  }
  */
}

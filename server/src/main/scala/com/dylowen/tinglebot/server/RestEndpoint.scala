package com.dylowen.tinglebot.server

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}
import com.dylowen.tinglebot.server.api.InCreateBrain

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */
object RestEndpoint extends JsonSupport with Directives {
  def route(implicit system: ActorSystem): Route = pathPrefix("v1") {
    pathPrefix("brains") {
      pathEndOrSingleSlash {
        get {
          complete("{[]}")
        } ~
          post {
            entity(as[InCreateBrain]) {
              test => {

                //val actor = system.actorSelection(system / "path")
                //actor ! test

                println(test.name)
                complete(201 -> test)
              }

            }
          }
      } ~
        path(IntNumber) { id => {
          get {
            complete("lkjsdf")
            //restResponse(s"""{"something": $id}""")
          }
        }
        } ~ complete(404, HttpEntity(ContentTypes.`application/json`, "{missing body}"))
    }
  }
}

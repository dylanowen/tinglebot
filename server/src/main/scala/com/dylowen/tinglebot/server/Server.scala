package com.dylowen.tinglebot.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.Deflate
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import com.dylowen.tinglebot.brain.NGram

import scala.io.StdIn

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */

object Server extends App {
  implicit val system = ActorSystem("tinglebot-server")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: Throwable =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(InternalServerError, entity = "An unknown exception occured"))
        }
    }

  val serverSource = Http(system).bind(interface = "localhost", port = 8080)

  val nGram = new NGram[String](List())

  val routes = encodeResponseWith(Deflate) {
    pathPrefix("rest") {
      RestEndpoint.route
    } ~ complete(404, HttpEntity(ContentTypes.`text/html(UTF-8)`, "404 not found"))
  }

  val (host, port) = ("localhost", 8080)
  val bindingFuture = Http().bindAndHandle(routes, host, port)

  def shutdown = system.terminate

  bindingFuture.onFailure {
    case ex: Exception =>
      println(f"Failed to bind to $host:$port", ex)
      shutdown
  }

  println(s"Server online at http://$host:$port/\nPress RETURN to stop...")

  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => shutdown)
}

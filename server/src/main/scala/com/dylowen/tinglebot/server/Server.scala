package com.dylowen.tinglebot.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.dylowen.tinglebot.brain.NGram

import scala.concurrent.Future

/**
  * TODO add description
  *
  * @author dylan.owen
  * @since Apr-2016
  */

object Server extends App {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val serverSource = Http(system).bind(interface = "localhost", port = 8080)

  val nGram = new NGram[String](List())

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`,
        "<html><body>Hello Tings!</body></html>"))

    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      HttpResponse(entity = "PONG!")

    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
      sys.error("BOOM!")

    case _: HttpRequest =>
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection.handleWithSyncHandler(requestHandler)
      // this is equivalent to
      // connection handleWith { Flow[HttpRequest] map requestHandler }
    }).run()
}

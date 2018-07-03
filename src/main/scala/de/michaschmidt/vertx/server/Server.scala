package de.michaschmidt.vertx.server

import io.vertx.core.logging.LoggerFactory
import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web._

import scala.collection.mutable


object Starter {
    def main(arg:Array[String]):Unit = {
        val vertx = Vertx.vertx()
        val server = new Server()
        vertx.deployVerticle(server)
    }
}

class Server extends ScalaVerticle {

    var LOGGER:io.vertx.core.logging.Logger =  LoggerFactory.getLogger("de.michaschmidt.vertx.server.Server")

    var subscriptions:mutable.HashMap[String, String] = mutable.HashMap()

    override def start(): Unit = {
        val httpserver = vertx.createHttpServer()
        
        var router = Router.router(vertx)

        router.route("/").handler((routingContext: io.vertx.scala.ext.web.RoutingContext) => {
            var resp = routingContext.response()

            resp.putHeader("content-type", "text/plain")

            resp.end("Greetings from vertx-scala")
        })

        router.route("/test").handler((routingContext: io.vertx.scala.ext.web.RoutingContext) => {
            var resp = routingContext.response()

            resp.putHeader("content-type", "text/plain")

            resp.end("path = " + routingContext.request().path().get)
        })

        httpserver.websocketHandler(handler => {
            var msgCounter = 0L
            LOGGER.info("connected to " + handler.binaryHandlerID(), "" )
            handler.accept()
            var timerId = 0L
            if (handler.path() == "/demo"){

                vertx.setPeriodic(1000,  id => {
                  timerId = id
                  var s = String.format("{ \"message\":\"test\", \"id\": %s}", (msgCounter+""))
                  msgCounter = msgCounter +1
                  handler.writeTextMessage(s)
                })
            }
            handler.closeHandler(ch => {
              vertx.cancelTimer(timerId)
            })

        })

        httpserver.requestHandler(router.accept _).listen(8080)
    }

    def handleMessage(){

    }

    def handleSocketInput(){

    }

}
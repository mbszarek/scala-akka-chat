package pl.edu.agh.chat.server

import akka.actor.{ActorSystem, Props}

object Application extends App {
  if (args.length == 0){
    println("Usage: ./program {port number}")
    System.exit(1)
  }
  val system = ActorSystem("system")
  val port = args(0).toInt
  println(port)
  val chatService = system.actorOf(Props(new Chat(port)))
}
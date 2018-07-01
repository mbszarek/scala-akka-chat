package pl.edu.agh.chat.server

import java.io.IOException
import java.net.{ServerSocket, Socket}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class Chat(portNumber: Int, var serverSocket: ServerSocket = null) extends Actor with ActorLogging {
  var list: List[ActorRef] = List[ActorRef]()
  var names: Map[String, ActorRef] = Map[String, ActorRef]()
  val system = ActorSystem("ChatSystem")

  override def preStart(): Unit = {
    try {
      this.serverSocket = new ServerSocket(portNumber)
      println(s"Starting server at $portNumber")
    } catch {
      case ioe: IOException => {
        println("There's a problem with opening server socket!")
        ioe.printStackTrace()
      }
    }
    ActorSystem().scheduler.schedule(0.milliseconds, 50.milliseconds, () => acceptClient())
  }


  def acceptClient(): Unit = {
    var clientSocket: Socket = null
    clientSocket = serverSocket.accept()
    if (clientSocket != null) {
      val newUser = this.system.actorOf(Props(new ChatUser(socket = clientSocket ,system = self)))
      log.info(newUser.toString())
      newUser ! AskForLogin
      this.list ::= newUser
    }
  }

  override def receive: Receive = {
    case message: Login =>
      if (message.message != null) {
        if (!this.names.contains(message.message)) {
          this.names = this.names + (message.message -> message.actorRef)
        } else {
          message.actorRef ! Reject
          Thread.sleep(100)
          system.stop(message.actorRef)
        }
      }
    case message: MessageToServer => this.list.foreach(x => x ! Broadcast(message.name + ": " + message.message + "\n"))
    case message: PrivateMessage =>
      val x = this.names.getOrElse(message.to, null)
      if (x != null) {
        val s = privateMessage(message.from, message.message)
        x ! Broadcast(s)
      } else {
        this.names(message.from) ! Broadcast("No such user!\n")
      }
    case message: ExitMessage =>
      val x = this.names.getOrElse(message.from, default = null)
      if (x != null) {
        log.info(message.from)
        log.info(x.toString())
        this.names = this.names.filterKeys(_ != message.from)
        this.list = this.list.filter(_ != x)
        system.stop(x)
      }
    case message: ListUsers => message.actorRef ! Broadcast(listUsers + "\n")
    case message: Help => message.actorRef ! Broadcast(help)
  }

  def listUsers: String = {
    val x = new StringBuilder
    for (i <- this.names.keys)
      x.append(i + ", ")
    x.toString
  }

  def help: String = {
    val x = new StringBuilder
    x.append("Write message to send\n")
    x.append("!to {user} {message} to send private message\n")
    x.append("!ls to list all users\n")
    x.append("!help to access help\n")
    x.toString
  }

  def privateMessage(from: String, message: String): String = {
    val x = new StringBuilder
    x.append("Private message from" + from + ": " + message + "\n")
    x.toString
  }
}

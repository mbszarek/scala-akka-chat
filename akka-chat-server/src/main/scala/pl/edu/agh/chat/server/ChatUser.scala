package pl.edu.agh.chat.server

import java.io._
import java.net._
import java.nio.charset.CodingErrorAction

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem}

import scala.concurrent.duration._
import scala.io._


class ChatUser(socket: Socket, var input: BufferedReader = null, var output: PrintStream = null, system: ActorRef)
  extends Actor with ActorLogging {
  var name: String = _
  implicit val codec: Codec = Codec("UTF-8")
  codec.onMalformedInput(CodingErrorAction.REPLACE)
  codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

  override def receive: Receive = {
    case null => log.info("Null message xD")
    case message: String if message.startsWith("!to") =>
      val x = "!to ([a-zA-Z0-9]+) (.+)".r
      val found = x.findAllIn(message)
      if (found.groupCount == 2) {
        system ! PrivateMessage(from = this.name, message = found.group(2), to = found.group(1))
        this.output.print("Sending private message\n")
      }
    case message: String if message.startsWith("!help") => system ! Help(self)
    case message: String if message.startsWith("!ls") => system ! ListUsers(self)
    case message: String => system ! MessageToServer(message, this.name)
    case message: Broadcast => this.output.print(message.message)
    case Reject => this.socket.close()
    case ExitMessage => system ! ExitMessage(this.name)
    case AskForLogin =>
      this.output.print("Give your nickname:\n")
      while (this.name == null)
        this.name = this.input.readLine()
      system ! Login(this.name, self)
    case _ => {}
  }

  override def preStart(): Unit = {
    this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream))
    this.output = new PrintStream(this.socket.getOutputStream)
    ActorSystem().scheduler.schedule(0.milliseconds, 50.milliseconds, () => readInput())
  }

  def readInput(): Unit = {
    try {
      if (this.name != null) {
        val x = this.input.readLine()
        if (x != null) {
          self ! x
        } else {
          self ! ExitMessage
        }
      }
    } catch {
      case _: SocketException => self ! ExitMessage
    }
  }
}

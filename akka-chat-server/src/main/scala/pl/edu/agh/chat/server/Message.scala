package pl.edu.agh.chat.server

import akka.actor.ActorRef

sealed trait Message

case class Broadcast(message: String) extends Message

case class Login(message: String, actorRef: ActorRef) extends Message

case class AskForLogin() extends Message

case class MessageToServer(message: String, name: String) extends Message

case class Help(actorRef: ActorRef) extends Message

case class ListUsers(actorRef: ActorRef) extends Message

case class PrivateMessage(from: String, message: String, to: String) extends Message

case class PrivateMessageFromServer(from: String, message: String) extends Message

case class ExitMessage(from: String) extends Message

case class Reject() extends Message

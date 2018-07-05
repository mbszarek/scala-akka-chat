package pl.edu.agh.chat

import java.io._
import java.net.Socket

class Client(var name: String = "", var socket: Socket = null, var os: PrintWriter = null,
             var is: BufferedReader = null, var port: Int = 0, var url: String = null) {

  def connect(): Unit = {
    try {
      this.socket = new Socket(url, port)
      this.os = new PrintWriter(this.socket.getOutputStream, true)
      this.is = new BufferedReader(new InputStreamReader(this.socket.getInputStream))
      this.os.println(Main.client.name)
    } catch {
      case ioe: IOException =>
        ioe.printStackTrace()
        println("Cannot connect to server!")
        System.exit(1)
    }
  }

  def sendMessage(string: String): Unit = {
    this.os.println(string)
  }

  def receiveMessage(): String = {
    var x = ""
    try {
      x = this.is.readLine()
    } catch {
      case _: Exception => println("Problem in receiving message")
    }
    x
  }
}

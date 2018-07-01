package pl.edu.agh.chat

import java.io.{BufferedReader, IOException, InputStreamReader, PrintStream}
import java.net.Socket

class Client(var name: String = "", var socket: Socket = null, var os: PrintStream = null,
             var is: BufferedReader = null, var port: Int = 0, var url: String = null) {

  def connect(): Unit = {
    try {
      this.socket = new Socket(url, port)
      this.os = new PrintStream(this.socket.getOutputStream)
      this.is = new BufferedReader(new InputStreamReader(this.socket.getInputStream))
      this.os.print(this.name)
    } catch {
      case ioe: IOException =>
        ioe.printStackTrace()
        println("Cannot connect to server!")
        System.exit(1)
    }
  }

  def sendMessage(string: String): Unit = {
    this.os.print(string)
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

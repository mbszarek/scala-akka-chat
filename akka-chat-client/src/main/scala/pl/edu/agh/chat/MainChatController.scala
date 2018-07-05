package pl.edu.agh.chat

import java.net.URL
import java.util.ResourceBundle

import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.{Label, TextArea, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class MainChatController (private var messagesText: TextArea,
                          private var insertText: TextField,
                          private var roomName: Label,
                          private var userName: Label,
                          private var isActive: Boolean = false ) {

  def sendMessage(event: ActionEvent): Unit = {
    if (!isActive) readMessages()
    if (!insertText.getText().equals("")) {
      println(insertText.getText)
      try {
        Main.client.sendMessage(insertText.getText())
      } catch {
        case _: Exception => println("Error while sending")
      }
      insertText.clear()
    }
  }

  def readMessages(): Unit = {
    isActive = true
    val thread = new Thread(() => {
      while(!Main.client.socket.isClosed) {
        val x = Main.client.receiveMessage()
        println(x)
        if (x != null)
          messagesText.appendText("\n")
          messagesText.appendText(x)
      }
    })
    thread.setDaemon(true)
    thread.start()
  }

  def quit(): Unit = {
    Platform.exit()
  }
}

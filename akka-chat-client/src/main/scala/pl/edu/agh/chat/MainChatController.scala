package pl.edu.agh.chat

import java.net.URL
import java.util.ResourceBundle

import javafx.application.Platform
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Label, TextArea, TextField}
import pl.edu.agh.chat.Views.{Hostname, Name, Port}

class MainChatController extends Initializable {
  @FXML
  var messagesText: TextArea = _

  @FXML
  var insertText: TextField = _

  @FXML
  var roomName: Label = _

  @FXML
  var userName: Label = _

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    Main.client.url = Hostname.display
    Main.client.port = Port.display
    Main.client.name = Name.display
    Main.client.connect()
    roomName.setText("Hello!")
    userName.setText(Main.client.name)
  }

  @FXML
  def sendMessage(): Unit = {
    if (!insertText.getText().equals("")) {
      try {
        Main.client.sendMessage(insertText.getText())
      } catch {
        case _: Exception => println("Error while sending")
      }
      insertText.clear()
    }
  }

  def readMessages(): Unit = {
    val thread = new Thread(() => {
      while(!Main.client.socket.isClosed) {
        val x = Main.client.receiveMessage()
        if (x != null)
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

package pl.edu.agh.chat

import java.io.IOException

import pl.edu.agh.chat.Views.{Hostname, Name, Port}
import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLView, NoDependencyResolver}

object Main extends JFXApp {
  var client = new Client()
  val resource = getClass.getResource("mainChat.fxml")
  if (resource == null) {
    throw new IOException("Cannot load mainChat.fxml")
  }
  val root = FXMLView(resource, NoDependencyResolver)
  client.url = Hostname.display
  client.port = Port.display
  client.name = Name.display
  client.connect()
  stage = new PrimaryStage() {
    title = "Chat"
    scene = new Scene(root)
    onCloseRequest = () => Platform.exit()
  }
}

//class Main extends Application{
//  override def start(primaryStage: Stage) = {
//    val mainChat = FXMLLoader.load(getClass.getResource("../../../../../resource/mainChat.fxml"))
//    primaryStage.setTitle("Chat")
//    primaryStage.setScene(new Scene(mainChat))
//    primaryStage.show()
//    primaryStage.setOnCloseRequest(_ => Platform.exit())
//  }
//}
//
//object Main {
//  var client = new Client()
//  def main(args: Array[String]): Unit = {
//    Application.launch(classOf[Main], args: _*)
//  }
//}

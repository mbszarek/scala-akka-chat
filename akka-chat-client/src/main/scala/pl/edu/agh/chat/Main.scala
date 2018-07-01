package pl.edu.agh.chat

import javafx.application.{Application, Platform}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class Main extends Application{
  override def start(primaryStage: Stage) = {
    val mainChat = FXMLLoader.load(getClass.getResource("../../../../../resource/mainChat.fxml"))
    primaryStage.setTitle("Chat")
    primaryStage.setScene(new Scene(mainChat))
    primaryStage.show()
    primaryStage.setOnCloseRequest(_ => Platform.exit())
  }
}

object Main {
  var client = new Client()
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}

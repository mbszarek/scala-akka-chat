package pl.edu.agh.chat.Views

import scalafx.geometry.Pos
import scalafx.stage.{Modality, Stage}
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.VBox
import scalafx.scene.Scene

class Port {
}

object Port {
  var port = 0
  def display: Int = {
    val window = new Stage()
    window.initModality(Modality.APPLICATION_MODAL)
    window.setTitle("Login")
    window.setMinWidth(400)
    val label = new Label("Insert port number")
    val field = new TextField()
    val button = new Button("next")
    button.setDefaultButton(true)
    button.setOnAction(_ => {
      if (!field.getText.equals(""))
        this.port = field.getText().toInt
      window.close()
    })
    val layout = new VBox(30)
    layout.getChildren.addAll(label, field, button)
    layout.setAlignment(Pos.CENTER)
    val scene = new Scene(layout)
    window.setScene(scene)
    window.showAndWait()
    port
  }
}
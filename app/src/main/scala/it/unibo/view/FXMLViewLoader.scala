package it.unibo.view

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

trait ViewLoader {
  def load[T](fxmlPath: String): (Parent, T)
}

class FXMLViewLoader extends ViewLoader {
  override def load[T](fxmlPath: String): (Parent, T) = {
    val loader = new FXMLLoader(getClass.getResource(fxmlPath))
    val root: Parent = loader.load()
    val controller: T = loader.getController
    (root, controller)
  }
}

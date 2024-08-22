package it.unibo.view

import javafx.fxml.FXMLLoader
import javafx.scene.Node

sealed trait ViewLoader:
  def load[T](fxmlPath: String, component: T): Node

object FXMLViewLoader extends ViewLoader:
  override def load[T](fxmlPath: String, component: T): Node =
    val loader = new FXMLLoader(getClass.getResource(fxmlPath))
    loader.setController(component)
    loader.load()

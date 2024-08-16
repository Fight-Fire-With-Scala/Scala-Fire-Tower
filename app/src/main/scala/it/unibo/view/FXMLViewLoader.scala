package it.unibo.view

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

sealed trait ViewLoader:
  def load[T](fxmlPath: String, component: T): Parent

object FXMLViewLoader extends ViewLoader:
  override def load[T](fxmlPath: String, component: T): Parent =
    val loader = new FXMLLoader(getClass.getResource(fxmlPath))
    loader.setController(component)
    loader.load()

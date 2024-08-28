package it.unibo.view

import it.unibo.view.components.IHaveView
import javafx.fxml.FXMLLoader
import javafx.scene.Node

sealed trait ViewLoader:
  def load[T <: IHaveView](component: T): Node

object FXMLViewLoader extends ViewLoader:
  override def load[T <: IHaveView](component: T): Node =
    val loader = new FXMLLoader(getClass.getResource(component.fxmlPath))
    loader.setController(component)
    loader.load()

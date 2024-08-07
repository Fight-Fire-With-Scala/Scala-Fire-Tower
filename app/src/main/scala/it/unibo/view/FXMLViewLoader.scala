package it.unibo.view

import it.unibo.view.components.GraphicComponent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

trait ViewLoader:
  def load[T](fxmlPath: String, component: T): Parent

class FXMLViewLoader extends ViewLoader:
  override def load[T](fxmlPath: String, component: T): Parent =
    val loader = new FXMLLoader(getClass.getResource(fxmlPath))
    loader.setController(component)
    //    val proxyController = new MenuControllerProxy(actualController)
    val root: Parent = loader.load()
    root

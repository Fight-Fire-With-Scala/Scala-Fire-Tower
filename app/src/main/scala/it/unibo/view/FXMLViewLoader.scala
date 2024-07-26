package it.unibo.view

import it.unibo.view.controllers.utils.{Notifier, StartGameNotifier}
import it.unibo.view.controllers.menu.{MenuController, MenuControllerService}
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

trait ViewLoader:
  def load[T](fxmlPath: String, controller: T): Parent

class FXMLViewLoader extends ViewLoader:
  override def load[T](fxmlPath: String, controller: T): Parent =
    val loader = new FXMLLoader(getClass.getResource(fxmlPath))
    loader.setController(controller)
    //    val proxyController = new MenuControllerProxy(actualController)
    val root: Parent = loader.load()
    root

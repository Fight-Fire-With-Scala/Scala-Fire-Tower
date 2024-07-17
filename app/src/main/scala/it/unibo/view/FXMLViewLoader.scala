package it.unibo.view

import it.unibo.view.controllers.utils.{Notifier, StartGameNotifier}
import it.unibo.view.controllers.menu.{MenuController, MenuControllerService}
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

trait ViewLoader:
  def load(fxmlPath: String): (Parent, MenuControllerService)

class FXMLViewLoader extends ViewLoader:
  override def load(fxmlPath: String): (Parent, MenuControllerService) =
    val loader = new FXMLLoader(getClass.getResource(fxmlPath))
    val notifier = new StartGameNotifier()

    val actualController: MenuControllerService = new MenuController(notifier)
    loader.setController(actualController)

    //    val proxyController = new MenuControllerProxy(actualController)
    val root: Parent = loader.load()

    (root, actualController)

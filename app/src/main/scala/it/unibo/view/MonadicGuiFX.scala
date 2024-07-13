package it.unibo.view

import it.unibo.controller.ControllerModule
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

import scala.compiletime.uninitialized

object GUIType extends Enumeration {
  val Menu: Value = Value

  def fxmlPath(value: GUIType.Value): String = value match { case Menu => "/pages/menu.fxml" }
}

class MonadicGuiFX(
    val w: Int,
    val h: Int,
    controller: ControllerModule.Controller,
    viewLoader: ViewLoader
) extends JFXApp3 {

  private var pane: Pane = uninitialized

  override def start(): Unit = {
    pane = new Pane()
    stage = new PrimaryStage {
      scene = new Scene(pane, w, h)
      minHeight = 600
      minWidth = 900
    }
    loadGUI(GUIType.Menu)
  }

  import scalafx.Includes._
  import scalafx.scene.Node

  def loadGUI(guiType: GUIType.Value): Unit = {
    val fxmlPath = GUIType.fxmlPath(guiType)
    val guiTask = Task {
      val (root, _) = viewLoader.load(fxmlPath)
      Platform.runLater { () =>
        pane.children.clear()
        pane.children.add(root)
        stage.show()
      }
    }
    guiTask.runAsyncAndForget
  }
}

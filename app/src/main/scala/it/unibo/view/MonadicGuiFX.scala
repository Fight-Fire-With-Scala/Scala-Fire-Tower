package it.unibo.view

import it.unibo.controller.ControllerModule
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import it.unibo.view.controllers.ControllerFactory

import scala.compiletime.uninitialized

class MonadicGuiFX(
    val w: Int,
    val h: Int,
    controller: ControllerModule.Controller,
    viewLoader: ViewLoader
) extends JFXApp3:

  private var pane: Pane = uninitialized

  override def start(): Unit =
    pane = new Pane()
    stage = new PrimaryStage:
      scene = new Scene(pane, w, h)
      minHeight = 600
      minWidth = 900
    loadGUI(GUIType.Menu)

  def loadGUI(guiType: GUIType): Unit =
    val guiTask = Task {
      val root = viewLoader
        .load(guiType.fxmlPath, ControllerFactory.createController(guiType)(controller))
      Platform.runLater { () =>
        pane.children.clear()
        pane.children.add(root.asInstanceOf[Node])
        stage.show()
      }
    }
    guiTask.runAsyncAndForget

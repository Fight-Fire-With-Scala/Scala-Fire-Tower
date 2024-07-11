package it.unibo.view

import it.unibo.controller.ControllerModule
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Button
import scalafx.scene.layout.{BorderPane, Pane}

class MonadicGuiFX(val w: Int, val h: Int, controller: ControllerModule.Controller)
    extends JFXApp3 {

  override def start(): Unit = {
    val guiTask = for {
      btn <- createButton("Click Me!")
      canvas <- createCanvas(w, h)
      pane <- createPanel(btn, canvas)
      _ <- Task(Platform.runLater(setupScene(pane)))
    } yield ()

    guiTask.runAsyncAndForget
  }

  private def createButton(text: String): Task[Button] = Task(new Button(text))

  private def createCanvas(width: Int, height: Int): Task[Canvas] = Task(new Canvas(width, height))

  private def createPanel(btn: Button, canvas: Canvas): Task[Pane] = Task {
    val pane = new BorderPane()
    pane.top = canvas
    pane.bottom = btn
    pane
  }

  private def setupScene(pane: Pane): Unit = stage = new PrimaryStage {
    scene = new Scene { root = pane }
    minHeight = 600
    minWidth = 900
  }
}

package it.unibo.view

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import it.unibo.controller.ViewSubject
import it.unibo.view.components.IMainComponent
import it.unibo.view.components.menu.MenuComponent
import javafx.concurrent.Task as JFXTask
import monix.eval.Task
import scalafx.scene.image.Image
import monix.execution.Scheduler.Implicits.global

import scala.compiletime.uninitialized
import scala.jdk.CollectionConverters.*

def wrapInMonixTask[T](jfxTask: JFXTask[T]): Task[T] = Task.async { cb =>
  jfxTask.setOnSucceeded(_ => cb.onSuccess(jfxTask.getValue))
  jfxTask.setOnFailed(_ => cb.onError(jfxTask.getException))
  jfxTask.run()
}

final class GameUIManager(val w: Int, val h: Int, viewObservable: ViewSubject) extends JFXApp3:

  private var pane: Pane = uninitialized

  override def start(): Unit =
    pane = new StackPane()
    stage = new PrimaryStage:
      title = "Scala Fire Tower"
      resizable = false
      icons += new Image(this.getClass.getResourceAsStream("/icon.png"))
      scene = new Scene(pane, w, h)
      minHeight = h
      minWidth = w

    loadGUIRoot(MenuComponent(viewObservable)).runAsyncAndForget

  def loadGUIRoot(componentInstance: IMainComponent): Task[IMainComponent] =
    val root = FXMLViewLoader.load(componentInstance)
    wrapInMonixTask[IMainComponent] { () =>
      pane.children.clear()
      pane.children.add(root)
      stage.show()
      componentInstance
    }

package it.unibo.view

import scala.compiletime.uninitialized
import scala.jdk.CollectionConverters._

import it.unibo.controller.ViewSubject
import it.unibo.view.component.IViewComponent
import it.unibo.view.component.menu.MenuComponent
import javafx.concurrent.{Task => JFXTask}
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.layout.Pane
import scalafx.scene.layout.StackPane

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

  def loadGUIRoot(componentInstance: IViewComponent): Task[IViewComponent] =
    val root = FXMLViewLoader.load(componentInstance)
    wrapInMonixTask { () =>
      pane.children.clear()
      pane.children.add(root)
      stage.show()
      componentInstance
    }

  private def wrapInMonixTask[T](jfxTask: JFXTask[T]): Task[T] = Task.async { cb =>
    jfxTask.setOnSucceeded(_ => cb.onSuccess(jfxTask.getValue))
    jfxTask.setOnFailed(_ => cb.onError(jfxTask.getException))
    jfxTask.run()
  }

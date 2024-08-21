package it.unibo.view

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import it.unibo.view.components.{ComponentFactory, GraphicComponent}
import it.unibo.controller.ViewSubject
import javafx.concurrent.Task
import scalafx.scene.image.Image

import scala.compiletime.uninitialized
import scala.jdk.CollectionConverters.*

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
    loadGuiComponent(GUIType.Menu).run()

  def loadGuiComponent(guiType: GUIType): Task[GraphicComponent] =
    val componentInstance = ComponentFactory.createFXMLComponent(guiType)(viewObservable)
    val root = FXMLViewLoader.load(guiType.fxmlPath, componentInstance)
    () =>
      pane.children.clear()
      pane.children.add(root)
      stage.show()
      componentInstance

package it.unibo.view

import it.unibo.controller.ControllerModule
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import it.unibo.view.components.{ComponentFactory, GraphicComponent, GraphicComponentRegistry}
import it.unibo.view.components.hand.HandComponent
import it.unibo.view.components.hand.cards.CardComponent
import monix.reactive.subjects.PublishSubject
import it.unibo.controller.ViewSubject

import scala.compiletime.uninitialized

class MonadicGuiFX(val w: Int, val h: Int, viewLoader: ViewLoader, observableSubject: ViewSubject)
    extends JFXApp3:

  private var pane: Pane = uninitialized

  override def start(): Unit =
    pane = new Pane()
    stage = new PrimaryStage:
      scene = new Scene(pane, w, h)
      minHeight = 720
      minWidth = 1280
    loadGUI(GUIType.Menu)

  private def loadGuiComponent(
      guiType: GUIType,
      setup: Option[GraphicComponent => Unit] = None
  ): Unit =
    val guiTask = Task {
      val componentInstance = ComponentFactory.createFXMLComponent(guiType)(observableSubject)
      GraphicComponentRegistry.register(componentInstance)
      val root = viewLoader.load(guiType.fxmlPath, componentInstance).asInstanceOf[Node]
      setup.foreach(_(componentInstance))
      Platform.runLater { () =>
        pane.children.clear()
        pane.children.add(root)
        stage.show()
      }
    }
    guiTask.runAsyncAndForget

  private def loadGUI(guiType: GUIType): Unit = loadGuiComponent(guiType)

  def loadGrid(): Unit = loadGUI(GUIType.Grid)

  def loadHand(): Unit = loadGuiComponent(
    GUIType.Hand,
    Some { graphicComponent =>
      val handComponent = graphicComponent.asInstanceOf[HandComponent]
      (0 to 4).foreach { slotIndex =>
        val cardComponent = ComponentFactory.createFXMLComponent(GUIType.Card)(observableSubject)
          .asInstanceOf[CardComponent]
        val cardView = viewLoader.load(GUIType.Card.fxmlPath, cardComponent).asInstanceOf[Node]
        handComponent.setupCard(cardView, cardComponent, slotIndex)
      }
    }
  )

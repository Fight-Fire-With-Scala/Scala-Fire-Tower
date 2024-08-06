package it.unibo.view

import it.unibo.controller.ControllerModule
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import it.unibo.view.controllers.{ComponentFactory, GraphicController, GraphicControllerRegistry}
import it.unibo.view.controllers.hand.HandController
import it.unibo.view.controllers.hand.cards.CardController
import monix.reactive.subjects.PublishSubject
import it.unibo.controller.ViewSubject

import scala.compiletime.uninitialized

class MonadicGuiFX(
    val w: Int,
    val h: Int,
    viewLoader: ViewLoader,
    observableSubject: ViewSubject
) extends JFXApp3:

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
      setup: Option[GraphicController => Unit] = None
  ): Unit =
    val guiTask = Task {
      val controllerInstance = ComponentFactory.createFXMLComponent(guiType)(observableSubject)
      GraphicControllerRegistry.register(controllerInstance)
      val root = viewLoader.load(guiType.fxmlPath, controllerInstance).asInstanceOf[Node]
      setup.foreach(_(controllerInstance))
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
    Some { graphicController =>
      val handController = graphicController.asInstanceOf[HandController]
      (0 to 4).foreach { slotIndex =>
        val cardController = ComponentFactory.createFXMLComponent(GUIType.Card)(observableSubject)
          .asInstanceOf[CardController]
        val cardView = viewLoader.load(GUIType.Card.fxmlPath, cardController).asInstanceOf[Node]
        handController.setupCard(cardView, cardController, slotIndex)
      }
    }
  )

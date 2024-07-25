package it.unibo.view

import it.unibo.controller.ControllerModule
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.Pane
import it.unibo.view.controllers.{ControllerFactory, GraphicController}
import it.unibo.view.controllers.hand.HandController
import it.unibo.view.controllers.hand.cards.CardController

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

  private def loadGuiComponent(
      guiType: GUIType,
      setup: Option[GraphicController => Unit] = None
  ): Unit =
    val guiTask = Task {
      val controllerInstance = ControllerFactory.createController(guiType)(controller)
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

  def loadHand(): Unit = loadGuiComponent(
    GUIType.Hand,
    Some { graphicController =>
      val handController = graphicController.asInstanceOf[HandController]
      (0 to 4).foreach { slotIndex =>
        val cardController = ControllerFactory.createController(GUIType.Card)(controller)
          .asInstanceOf[CardController]
        val cardView = viewLoader.load(GUIType.Card.fxmlPath, cardController).asInstanceOf[Node]
        handController.addCardToSlot(cardView, cardController, slotIndex)
      }
    }
  )

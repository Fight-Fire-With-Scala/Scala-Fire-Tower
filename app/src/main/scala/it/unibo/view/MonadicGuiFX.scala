package it.unibo.view

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import it.unibo.view.components.{ComponentFactory, GraphicComponent, GraphicComponentRegistry}
import it.unibo.view.components.hand.HandComponent
import it.unibo.view.components.hand.cards.CardComponent
import it.unibo.controller.ViewSubject

import scala.compiletime.uninitialized
import scala.jdk.CollectionConverters.*

class MonadicGuiFX(val w: Int, val h: Int, viewLoader: ViewLoader, observableSubject: ViewSubject)
    extends JFXApp3:

  private var pane: Pane = uninitialized

  override def start(): Unit =
    pane = new StackPane()
    stage = new PrimaryStage:
      scene = new Scene(pane, w, h)
      minHeight = 720
      minWidth = 1280
    loadHand()

  private def loadGuiComponent(guiType: GUIType, setups: (GraphicComponent => Unit)*): Unit =
    val guiTask = Task {
      val componentInstance = ComponentFactory.createFXMLComponent(guiType)(observableSubject)
      GraphicComponentRegistry.register(componentInstance)
      val root = viewLoader.load(guiType.fxmlPath, componentInstance).asInstanceOf[Node]
      setups.foreach(_(componentInstance))
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
    graphicComponent =>
      val handComponent = graphicComponent.asInstanceOf[HandComponent]
      handComponent.handPane.getChildren.asScala.zipWithIndex.foreach { case (_, slotIndex) =>
        val cardComponent = ComponentFactory.createFXMLComponent(GUIType.Card)(observableSubject)
          .asInstanceOf[CardComponent]
        val cardView = viewLoader.load(GUIType.Card.fxmlPath, cardComponent).asInstanceOf[Node]
        handComponent.setupCard(cardView, cardComponent, slotIndex)      }

  )

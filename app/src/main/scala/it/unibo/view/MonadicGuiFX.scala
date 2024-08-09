package it.unibo.view

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import it.unibo.view.components.{ComponentFactory, GraphicComponent}
import it.unibo.view.components.hand.HandComponent
import it.unibo.view.components.hand.cards.CardComponent
import it.unibo.controller.ViewSubject
import it.unibo.view.components.deck.DeckComponent
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.gameboard.GridComponent

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
    loadGUI(GUIType.Menu)

  private def loadGuiComponent(guiType: GUIType, setups: (GraphicComponent => Unit)*): Unit =
    val guiTask = Task {
      val componentInstance = ComponentFactory.createFXMLComponent(guiType)(observableSubject)
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

  private def loadHand(): (HandComponent, Node) =
    val handComponent = ComponentFactory.createFXMLComponent(GUIType.Hand)(observableSubject)
      .asInstanceOf[HandComponent]
    val handView = viewLoader.load(GUIType.Hand.fxmlPath, handComponent).asInstanceOf[Node]
    handComponent.handPane.getChildren.asScala.zipWithIndex.foreach { case (_, slotIndex) =>
      val cardComponent = ComponentFactory.createFXMLComponent(GUIType.Card)(observableSubject)
        .asInstanceOf[CardComponent]
      val cardView = viewLoader.load(GUIType.Card.fxmlPath, cardComponent).asInstanceOf[Node]
      handComponent.setupCard(cardView, cardComponent, slotIndex)
    }
    (handComponent, handView)

  def loadGame(): Unit = loadGuiComponent(
    GUIType.Game,
    graphicComponent =>
      val gameComponent = graphicComponent.asInstanceOf[GameComponent]
      val gridComponent = ComponentFactory.createFXMLComponent(GUIType.Grid)(observableSubject)
        .asInstanceOf[GridComponent]
      val gridView = viewLoader.load(GUIType.Grid.fxmlPath, gridComponent).asInstanceOf[Node]
      gameComponent.setupGrid(gridView, gridComponent)

      val sidebarComponent = ComponentFactory.createFXMLComponent(GUIType.Deck)(observableSubject)
        .asInstanceOf[DeckComponent]
      val sidebarView = viewLoader.load(GUIType.Deck.fxmlPath, sidebarComponent).asInstanceOf[Node]
      gameComponent.setupSidebar(sidebarView, sidebarComponent)

      val (handComponent, handView) = loadHand()
      gameComponent.setupHand(handView, handComponent)

      GameBoardController.initialize(gameComponent)
  )

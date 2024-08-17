package it.unibo.view

import javafx.scene.Node
import scalafx.application.{JFXApp3, Platform}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.layout.{Pane, StackPane}
import it.unibo.view.components.{ComponentFactory, GraphicComponent}
import it.unibo.controller.ViewSubject
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.{CardComponent, HandComponent}
import it.unibo.view.components.game.gameboard.sidebar.{
  DeckComponent,
  GameInfoComponent,
  SidebarComponent,
  WindRoseComponent
}
import scalafx.scene.image.Image

import scala.compiletime.uninitialized
import scala.jdk.CollectionConverters.*

final class MonadicGuiFX(val w: Int, val h: Int, viewObservable: ViewSubject) extends JFXApp3:

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
    loadGUI(GUIType.Menu)

  private def loadGuiComponent(guiType: GUIType, setups: (GraphicComponent => Unit)*): Unit =
    val componentInstance = ComponentFactory.createFXMLComponent(guiType)(viewObservable)
    val root = FXMLViewLoader.load(guiType.fxmlPath, componentInstance).asInstanceOf[Node]
    setups.foreach(_(componentInstance))
    Platform.runLater { () =>
      pane.children.clear()
      pane.children.add(root)
      stage.show()
    }

  private def loadGUI(guiType: GUIType): Unit = loadGuiComponent(guiType)

  private def loadHand(): (HandComponent, Node) =
    val cardComponents = List.fill(5)(new CardComponent())
    val handComponent = HandComponent(cardComponents)
    val handView = FXMLViewLoader.load(GUIType.Hand.fxmlPath, handComponent)
    (handComponent, handView)

  private def loadSidebar(): (SidebarComponent, Node) =
    given observable: ViewSubject = viewObservable
    val subComponents = List(WindRoseComponent(), DeckComponent(), GameInfoComponent())
    val sidebarComponent = SidebarComponent(subComponents)
    val sidebarView = FXMLViewLoader.load(GUIType.Sidebar.fxmlPath, sidebarComponent)
    (sidebarComponent, sidebarView)

  private def loadGrid(): (GridComponent, Node) =
    val gridComponent = ComponentFactory.createFXMLComponent(GUIType.Grid)(viewObservable)
      .asInstanceOf[GridComponent]
    val gridView = FXMLViewLoader.load(GUIType.Grid.fxmlPath, gridComponent)
    (gridComponent, gridView)

  def loadGame(): Unit = loadGuiComponent(
    GUIType.Game,
    graphicComponent =>
      val gameComponent = graphicComponent.asInstanceOf[GameComponent]

      val (gridComponent, gridView) = loadGrid()
      gameComponent.setupGrid(gridView, gridComponent)

      val (sidebarComponent, sidebarView) = loadSidebar()
      gameComponent.setupSidebar(sidebarView, sidebarComponent)

      val (handComponent, handView) = loadHand()
      gameComponent.setupHand(handView, handComponent)

      GameBoardController.initialize(gameComponent)
  )

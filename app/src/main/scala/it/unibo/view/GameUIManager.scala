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

  private def loadHand(): HandComponent =
    val cardComponents = List.fill(5)(new CardComponent())
    val handComponent = HandComponent(cardComponents)
    handComponent

  private def loadSidebar(): SidebarComponent =
    given observable: ViewSubject = viewObservable
    val subComponents = List(WindRoseComponent(), DeckComponent(), GameInfoComponent())
    SidebarComponent(subComponents)

  private def loadGrid(): GridComponent =
    new GridComponent(viewObservable)

  def loadGame(): Unit = loadGuiComponent(
    GUIType.Game,
    graphicComponent =>
      val gameComponent = graphicComponent.asInstanceOf[GameComponent]

      gameComponent.setupGrid(loadGrid())
      gameComponent.setupSidebar(loadSidebar())
      gameComponent.setupHand(loadHand())

      GameBoardController.initialize(gameComponent)
  )

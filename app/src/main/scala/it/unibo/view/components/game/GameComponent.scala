package it.unibo.view.components.game

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.{GameBoard, GamePhase}
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.player.Player
import it.unibo.model.logger
import it.unibo.view.GUIType
import it.unibo.view.components.{IUpdateView, IViewComponent}
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.HandComponent
import it.unibo.view.components.game.gameboard.sidebar.{GameInfoComponent, SidebarComponent}
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.Node
import monix.eval.Task

import scala.compiletime.uninitialized

final class GameComponent extends IViewComponent with IUpdateView:
  override val fxmlPath: String = GUIType.Game.fxmlPath

  @FXML
  var grid: Pane = uninitialized
  @FXML
  var sidebar: Pane = uninitialized
  @FXML
  var hand: Pane = uninitialized

  var gridComponent: GridComponent = uninitialized
  var sidebarComponent: SidebarComponent = uninitialized
  var handComponent: HandComponent = uninitialized

  override def onEnableView(): Unit =
    super.onEnableView()
    gridComponent.enableView()
    sidebarComponent.enableView()
    handComponent.enableView()

  override def onDisableView(): Unit =
    super.onDisableView()
    gridComponent.disableView()
    sidebarComponent.disableView()
    handComponent.disableView()

  override protected def getPane: Node = grid

  private def setupComponent[T <: IViewComponent](
      pane: Pane,
      componentPane: Node,
      assignComponent: () => IViewComponent
  ): Task[IViewComponent] = runTaskOnUIThread {
    pane.getChildren.add(componentPane)
    assignComponent()
  }

  def setupGrid(gc: GridComponent): Task[IViewComponent] =
    val gridView: Node = gc.getView
    setupComponent(
      grid,
      gridView,
      () =>
        gridComponent = gc
        gc
    )

  def setupSidebar(sc: SidebarComponent): Task[IViewComponent] =
    val sidebarView: Node = sc.getView
    setupComponent(
      sidebar,
      sidebarView,
      () =>
        sidebarComponent = sc
        sc
    )

  def setupHand(hc: HandComponent): Task[IViewComponent] =
    val handView: Node = hc.getView
    setupComponent(
      hand,
      handView,
      () =>
        handComponent = hc
        hc
    )
  
  def updateGrid(gameBoard: GameBoard, gamePhase: GamePhase): Unit =
    val grid = gameBoard.board.grid
    val currentPlayer = gameBoard.getCurrentPlayer()
    val currentTowerPositions = grid.getTowerCells(currentPlayer.towerPositions)
    gridComponent.updateGrid(grid, currentTowerPositions, gamePhase)

  def updatePlayer(player: Player)(gamePhase: GamePhase): Unit = runOnUIThread {
    val updatedHand = player.extraCard.fold(player.hand)(card => player.hand :+ card)
    handComponent.updateHand(updatedHand)(gamePhase)
  }

  def updateSidebar(gameInfoComponent: GameInfoComponent, newGamePhase: GamePhase): Unit =
    gameInfoComponent.updateTurnPhase(newGamePhase.toString)

object GameComponent extends GameComponentInitializer:
  override def initialize(gameComponent: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent] = loadGame(gameComponent)

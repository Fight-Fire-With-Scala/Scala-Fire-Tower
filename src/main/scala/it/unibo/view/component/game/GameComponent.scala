package it.unibo.view.component.game

import scala.compiletime.uninitialized

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.player.Player
import it.unibo.view.GUIType
import it.unibo.view.component.IUpdateView
import it.unibo.view.component.ViewComponent
import it.unibo.view.component.game.gameboard.grid.GridComponent
import it.unibo.view.component.game.gameboard.hand.HandComponent
import it.unibo.view.component.game.gameboard.sidebar.GameInfoComponent
import it.unibo.view.component.game.gameboard.sidebar.SidebarComponent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane
import monix.eval.Task

final class GameComponent extends ViewComponent with IUpdateView:
  override val fxmlPath: String = GUIType.Game.fxmlPath

  @FXML
  var grid: Pane = uninitialized
  @FXML
  var sidebar: Pane = uninitialized
  @FXML
  var hand: Pane = uninitialized

  var gridComponent: GridComponent       = uninitialized
  var sidebarComponent: SidebarComponent = uninitialized
  var handComponent: HandComponent       = uninitialized

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

  private def setupComponent[T <: ViewComponent](
      pane: Pane,
      componentPane: Node,
      assignComponent: () => ViewComponent
  ): Task[ViewComponent] = runTaskOnUIThread:
    pane.getChildren.add(componentPane)
    assignComponent()

  def setupGrid(gc: GridComponent): Task[ViewComponent] =
    val gridView: Node = gc.getView
    setupComponent(
      grid,
      gridView,
      () =>
        gridComponent = gc
        gc
    )

  def setupSidebar(sc: SidebarComponent): Task[ViewComponent] =
    val sidebarView: Node = sc.getView
    setupComponent(
      sidebar,
      sidebarView,
      () =>
        sidebarComponent = sc
        sc
    )

  def setupHand(hc: HandComponent): Task[ViewComponent] =
    val handView: Node = hc.getView
    setupComponent(
      hand,
      handView,
      () =>
        handComponent = hc
        hc
    )

  def updateGrid(gameBoard: GameBoard, gamePhase: GamePhase): Unit =
    val grid                  = gameBoard.board.grid
    val currentPlayer         = gameBoard.getCurrentPlayer
    val currentTowerPositions = grid.getTowerCells(currentPlayer.towerPositions)
    gridComponent.updateGrid(grid, currentTowerPositions, gamePhase)

  def updatePlayer(player: Player)(gamePhase: GamePhase): Unit = runOnUIThread:
    val updatedHand = player.extraCard.fold(player.hand)(card => player.hand :+ card)
    handComponent.updateHand(updatedHand)(gamePhase)

  def updateSidebar(gameInfoComponent: GameInfoComponent, newGamePhase: GamePhase): Unit =
    gameInfoComponent.updateTurnPhase(newGamePhase.toString)

object GameComponent extends GameComponentInitializer:
  override def initialize(gameComponent: GameComponent)(using
      viewObservable: ViewSubject,
      internalViewObservable: InternalViewSubject
  ): Task[GameComponent] = loadGame(gameComponent)

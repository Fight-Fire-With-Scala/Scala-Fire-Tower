package it.unibo.view.components.game

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.players.Player
import it.unibo.view.GUIType
import it.unibo.view.components.{IMainComponent, IUpdateView}
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.HandComponent
import it.unibo.view.components.game.gameboard.sidebar.{GameInfoComponent, SidebarComponent}
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.Node
import monix.eval.Task

import scala.compiletime.uninitialized

final class GameComponent extends IMainComponent with IUpdateView:
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

  private def setupComponent[T <: IMainComponent](
      pane: Pane,
      componentPane: Node,
      assignComponent: () => IMainComponent
  ): Task[IMainComponent] = runOnUIThread {
      pane.getChildren.add(componentPane)
      assignComponent()
  }

  def setupGrid(gc: GridComponent): Task[IMainComponent] =
    val gridView: Node = gc.getView
    setupComponent(grid, gridView, () => {
      gridComponent = gc
      gc
    })

  def setupSidebar(sc: SidebarComponent): Task[IMainComponent] =
    val sidebarView: Node = sc.getView
    setupComponent(sidebar, sidebarView, () => {
      sidebarComponent = sc
      sc
    })

  def setupHand(hc: HandComponent): Task[IMainComponent] =
    val handView: Node = hc.getView
    setupComponent(hand, handView, () => {
      handComponent = hc
      hc
    })

  def updateGrid(grid: Grid): Unit = runOnUIThread(gridComponent.updateGrid(grid))

  def updatePlayer(player: Player): Unit = runOnUIThread {
    val updatedHand = player.extraCard.fold(player.hand)(card => player.hand :+ card)
    handComponent.updateHand(updatedHand)
  }

  def updateSidebar(gameInfoComponent: GameInfoComponent, newGamePhase: GamePhase): Unit =
    gameInfoComponent.updateTurnPhase(newGamePhase.toString)

object GameComponent extends GameComponentInitializer:
  override def initialize(
      gameComponent: GameComponent
  )(using viewObservable: ViewSubject, internalViewObservable: InternalViewSubject): Task[GameComponent] =
    loadGame(gameComponent)

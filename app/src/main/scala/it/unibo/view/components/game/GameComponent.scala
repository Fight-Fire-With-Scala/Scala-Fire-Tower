package it.unibo.view.components.game

import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.players.Player
import it.unibo.view.components.GraphicComponent
import it.unibo.view.components.game.gameboard.grid.GridComponent
import it.unibo.view.components.game.gameboard.hand.HandComponent
import it.unibo.view.components.game.gameboard.sidebar.SidebarComponent
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import scalafx.application.Platform
import javafx.scene.Node

import scala.compiletime.uninitialized

final class GameComponent extends GraphicComponent:
  @FXML
  var grid: Pane = uninitialized
  @FXML
  var sidebar: Pane = uninitialized
  @FXML
  var hand: Pane = uninitialized
  
  var gridComponent: GridComponent = uninitialized
  var sidebarComponent: SidebarComponent = uninitialized
  var handComponent: HandComponent = uninitialized

  private def setupComponent[T <: GraphicComponent](
      pane: Pane,
      componentPane: Node,
      assignComponent: () => Unit
  ): Unit = Platform.runLater { () =>
    pane.getChildren.add(componentPane)
    assignComponent()
  }

  def setupGrid(gridPane: Node, gridController: GridComponent): Unit =
    setupComponent(grid, gridPane, () => gridComponent = gridController)

  def setupSidebar(sidebarPane: Node, sidebarController: SidebarComponent): Unit =
    setupComponent(sidebar, sidebarPane, () => sidebarComponent = sidebarController)

  def setupHand(handPane: Node, handController: HandComponent): Unit =
    setupComponent(hand, handPane, () => handComponent = handController)

  def updateGrid(grid: Grid): Unit = Platform.runLater(() => gridComponent.updateGrid(grid))

  def updatePlayer(player: Player): Unit = Platform
    .runLater(() => handComponent.updateHand(player.hand))

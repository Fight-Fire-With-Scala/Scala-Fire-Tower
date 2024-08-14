package it.unibo.view.components.game

import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.players.Player
import it.unibo.view.components.GraphicComponent
import it.unibo.view.components.gameboard.GridComponent
import it.unibo.view.components.hand.HandComponent
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import scalafx.application.Platform
import javafx.scene.Node

import scala.compiletime.uninitialized

class GameComponent extends GraphicComponent:
  @FXML
  var grid: Pane = uninitialized
  @FXML
  var sidebar: Pane = uninitialized
  @FXML
  var hand: Pane = uninitialized

  var gridComponent: GridComponent = uninitialized
  var sidebarComponent: GraphicComponent = uninitialized
  var handComponent: HandComponent = uninitialized

  private def setupComponent[T <: GraphicComponent](
      pane: Pane,
      componentPane: Node,
      assignComponent: () => Unit
  ): Unit = Platform.runLater { () =>
    pane.getChildren.clear()
    pane.getChildren.add(componentPane)
    assignComponent()
  }

  def setupGrid(gridPane: Node, gridController: GridComponent): Unit =
    setupComponent(grid, gridPane, () => gridComponent = gridController)

  def setupSidebar(sidebarPane: Node, sidebarController: GraphicComponent): Unit =
    setupComponent(sidebar, sidebarPane, () => sidebarComponent = sidebarController)

  def setupHand(handPane: Node, handController: HandComponent): Unit =
    setupComponent(hand, handPane, () => handComponent = handController)

  def updateGrid(grid: Grid): Unit = Platform.runLater(() => gridComponent.updateGrid(grid))

  def updatePlayer(player: Player): Unit = Platform
    .runLater(() => handComponent.updateHand(player.hand))

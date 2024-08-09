package it.unibo.view.components.game

import it.unibo.model.gameboard.grid.Grid
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

  private var gridComponent: GridComponent = uninitialized
  private var sidebarComponent: GraphicComponent = uninitialized
  private var handComponent: HandComponent = uninitialized

  private def setupComponent[T <: GraphicComponent](
      pane: Pane,
      componentPane: Node,
      componentController: T,
      componentVar: T => Unit
  ): Unit = Platform.runLater { () =>
    pane.getChildren.clear()
    pane.getChildren.add(componentPane)
    componentVar(componentController)
  }

  def setupGrid(gridPane: Node, gridController: GridComponent): Unit =
    setupComponent(grid, gridPane, gridController, gridComponent = _)

  def setupSidebar(sidebarPane: Node, sidebarController: GraphicComponent): Unit =
    setupComponent(sidebar, sidebarPane, sidebarController, sidebarComponent = _)

  def setupHand(handPane: Node, handController: HandComponent): Unit =
    setupComponent(hand, handPane, handController, handComponent = _)

  def updateGrid(grid: Grid): Unit = Platform.runLater(() => gridComponent.updateGrid(grid))

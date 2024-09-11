package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.view.GUIType
import it.unibo.view.components.{IGridComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.StackPane

import scala.compiletime.uninitialized

final class GridComponent(using
    internalObservable: InternalViewSubject,
    observableSubject: ViewSubject
) extends IGridComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Grid.fxmlPath

  private val cellNumber = 16
  private val cellSize = 42

  @FXML
  private var container: StackPane = uninitialized
  private var gridManager: GridManager = uninitialized

  @FXML
  def initialize(): Unit =
    gridManager = new GridManager(cellNumber, cellSize, internalObservable, observableSubject)
    gridManager.initialize(container)

  def setAvailablePatterns(patterns: Set[Map[Position, Token]], cardEffect: Int): Unit = gridManager
    .setAvailablePatterns(patterns, cardEffect)

  override def onEnableView(): Unit = gridManager.squareMap.foreach { case (_, square) =>
    square.enableView()
  }

  override def onDisableView(): Unit = gridManager.squareMap.foreach { case (_, square) =>
    square.disableView()
  }

  override protected def getPane: Node = container

  def updateGrid(grid: Grid, currentTowerPositions: Set[Position], gamePhase: GamePhase): Unit =
    gridManager.updateGrid(grid, currentTowerPositions, gamePhase)
package it.unibo.view.component.game.gameboard.grid

import scala.collection.mutable
import scala.compiletime.uninitialized

import it.unibo.controller.ViewSubject
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.Cell.EternalFire
import it.unibo.model.gameboard.grid.Cell.Tower
import it.unibo.model.gameboard.grid.Cell.Woods
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.view.component.game.gameboard.grid.handler.GridEventHandler
import javafx.scene.layout.StackPane
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color

class GridManager(
    gridSize: Int,
    squareSize: Double,
    observableSubject: ViewSubject
):
  private var gamePhase: GamePhase                 = uninitialized
  var squareMap: mutable.Map[Position, GridSquare] = mutable.Map()
  private var gridEventHandler: GridEventHandler   = uninitialized

  def initialize(container: StackPane): Unit =
    val gridInitializer = new GridInitializer(
      gridSize,
      squareSize,
      handleCellHover,
      handleCellClick
    )
    val gridPane = new GridPane
    squareMap = gridInitializer.initializeGridSquares(gridPane)
    gridEventHandler = new GridEventHandler(observableSubject, squareMap)
    container.getChildren.add(gridPane)

  private def handleCellClick(row: Int, col: Int): Unit =
    gridEventHandler.handleCellClick(row, col, gamePhase)
  private def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    gridEventHandler.handleCellHover(row, col, hoverDirection, gamePhase)

  def setAvailablePatterns(patterns: Set[Map[Position, Token]], cardEffect: Int): Unit =
    gridEventHandler.updateAvailablePatterns(patterns)
    gridEventHandler.setEffectCode(cardEffect)

  def updateGrid(grid: Grid, currentTowerPositions: Set[Position], gamePhase: GamePhase): Unit =
    squareMap.foreach { case (position, square) =>
      this.gamePhase = gamePhase
      val cellColor = grid.getCell(position) match
        case Some(_: Woods.type) => Color.DarkGreen
        case Some(_: Tower.type) if !currentTowerPositions.contains(position) =>
          Color.rgb(131, 18, 22)
        case Some(_: Tower.type)       => Color.rgb(76, 39, 3)
        case Some(_: EternalFire.type) => Color.Red
        case _                         => Color.White

      val tokenColor = grid.getToken(position) match
        case Some(token: Token) => token.color
        case _                  => cellColor

      runOnUIThread(square.updateColor(tokenColor))
    }

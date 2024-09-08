package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.launcher.Launcher.view.runOnUIThread
import it.unibo.model.cards.Card
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.grid.Cell.{EternalFire, Tower, Woods}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak, Water}
import it.unibo.model.gameboard.grid.{ConcreteToken, Grid, Position, Token, TowerPosition}
import it.unibo.view.components.game.gameboard.grid.handler.GridEventHandler
import it.unibo.view.logger
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized

class GridManager(
    gridSize: Int,
    squareSize: Double,
    internalObservable: InternalViewSubject,
    observableSubject: ViewSubject
):
  private var gamePhase: GamePhase = uninitialized
  var squareMap: mutable.Map[Position, GridSquare] = mutable.Map()
  private var gridEventHandler: GridEventHandler = uninitialized

  def initialize(container: StackPane): Unit =
    val gridInitializer = new GridInitializer(
      gridSize,
      squareSize,
      handleCellHover,
      handleCellClick
    )
    val gridPane = new GridPane
    squareMap = gridInitializer.initializeGridSquares(gridPane)
    gridEventHandler = new GridEventHandler(observableSubject, internalObservable, squareMap)
    container.getChildren.add(gridPane)

  private def handleCellClick(row: Int, col: Int): Unit = gridEventHandler.handleCellClick(row, col, gamePhase)
  private def handleCellHover(row: Int, col: Int, hoverDirection: HoverDirection): Unit =
    gridEventHandler.handleCellHover(row, col, hoverDirection, gamePhase)

  def setAvailablePatterns(patterns: Set[Map[Position, Token]], cardEffect: Int): Unit =
    gridEventHandler.updateAvailablePatterns(patterns)
    gridEventHandler.setEffectCode(cardEffect)

  def updateGrid(grid: Grid, gamePhase: GamePhase): Unit = squareMap
    .foreach { case (position, square) =>
      //gridEventHandler.updateGamePhase(gamePhase)
      this.gamePhase = gamePhase
      val cellColor = grid.getCell(position) match
        case Some(_: Woods.type)       => Color.DarkGreen
        case Some(_: Tower.type)       => Color.rgb(76, 39, 3)
        case Some(_: EternalFire.type) => Color.Red
        case _                         => Color.White

      val tokenColor = grid.getToken(position) match
        case Some(token: Token) => token.color
        case _ => cellColor

      runOnUIThread(square.updateColor(tokenColor))
    }

  def updateTowerColors(currentTower: TowerPosition, otherTowers: Set[TowerPosition]): Unit =
    val currentColor = Color.Blue
    val otherColor = Color.Gray
    logger.info(currentTower.squarePositions.toString)
    // Update the color for the current tower position
    currentTower.squarePositions.foreach { pos =>
      squareMap.get(pos).foreach(_.updateColor(currentColor))
    }

    // Update the color for the other tower positions
    otherTowers.foreach { tower =>
      tower.squarePositions.foreach { pos =>
        squareMap.get(pos).foreach(_.updateColor(otherColor))
      }
    }

package it.unibo.view.components.game.gameboard.grid

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{Grid, Position, Token}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.gameboard.grid.ConcreteToken.*
import it.unibo.view.components.GraphicComponent
import it.unibo.view.components.game.GameComponent
import javafx.fxml.FXML
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.application.Platform
import scalafx.scene.Node
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized
import scala.language.postfixOps

//noinspection VarCouldBeVal
final class GridComponent(observableSubject: ViewSubject) extends GraphicComponent:

  @FXML
  private var container: StackPane = uninitialized
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16
  private val squareSize = 42
  private val squareMap: mutable.Map[Position, GridSquare] = mutable.Map()
  var availablePatterns: List[Map[Position, Token]] = List.empty

  @FXML
  def initialize(): Unit =
    gridPane = new GridPane
    for {
      row <- 0 until gridSize
      col <- 0 until gridSize
    } {
      val square = GridSquare(row, col, squareSize, handleCellHover)
      GridPane.setRowIndex(square.getGraphicPane, row)
      GridPane.setColumnIndex(square.getGraphicPane, col)
      gridPane.children.add(square.getGraphicPane)
      squareMap(Position(row, col)) = square
    }
    container.getChildren.add(gridPane)

  private def handleCellHover(row: Int, col: Int, direction: HoverDirection): Unit =
    println(s"Hovering over square at row $row, col $col")
    println(direction)

    val hoverColor = Color.rgb(255, 0, 0, 0.5)

    val positionToCheck = checkNeighbor(Position(row, col), direction.direction)
    val candidatePositions = availablePatterns.filter(_.contains(positionToCheck))

    candidatePositions.foreach { pattern =>
      if (pattern.keys.exists(_ == positionToCheck))
        val square = squareMap(positionToCheck)
        Platform.runLater(() => square.updateColor(hoverColor))
    }

  private def checkNeighbor(startPosition: Position, direction: Direction): Position =
    startPosition - direction.getDelta

  def updateGrid(grid: Grid): Unit = squareMap.foreach { case (position, square) =>
    val cellColor = grid.getCell(position) match
      case Some(_: Woods.type)       => Color.DarkGreen
      case Some(_: Tower.type)       => Color.rgb(76, 39, 3)
      case Some(_: EternalFire.type) => Color.Red
      case _                         => Color.White

    val tokenColor = grid.getToken(position) match
      case Some(Fire)      => Color.Orange
      case Some(Firebreak) => Color.Blue
      case _               => cellColor

    Platform.runLater(() => square.updateColor(tokenColor))
  }

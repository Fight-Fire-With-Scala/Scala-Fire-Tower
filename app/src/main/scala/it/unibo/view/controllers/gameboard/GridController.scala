package it.unibo.view.controllers.gameboard

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.grid.{EternalFire, Fire, Firebreak, Grid, Position, Tower, Woods}
import it.unibo.view.controllers.GraphicController
import javafx.fxml.FXML
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.application.Platform
import scalafx.scene.paint.Color

import scala.collection.mutable
import scala.compiletime.uninitialized
import scala.language.postfixOps

class GridController(observableSubject: ViewSubject) extends GraphicController:

  @FXML
  private var container: StackPane = uninitialized
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16
  private val squareSize = 45
  private val squareMap: mutable.Map[Position, GridSquare] = mutable.Map()

  @FXML
  def initialize(): Unit =
    gridPane = new GridPane
    for {
      row <- 0 until gridSize
      col <- 0 until gridSize
    } {
      val square = GridSquare(row, col, squareSize, handleRectangleHover)
      GridPane.setRowIndex(square.getGraphicRectangle, row)
      GridPane.setColumnIndex(square.getGraphicRectangle, col)
      gridPane.children.add(square.getGraphicRectangle)
      squareMap(Position(row, col)) = square
    }
    container.getChildren.add(gridPane)

  private def handleRectangleHover(row: Int, col: Int, direction: HoverDirection): Unit =
    println(s"Hovering over square at row $row, col $col")
    println(direction)

    val hoverColor = Color.rgb(255, 0, 0, 0.5)

    val adjacentCells = direction match
      case HoverDirection.North => Seq(Position(row - 1, col), Position(row - 2, col))
      case HoverDirection.South => Seq(Position(row + 1, col), Position(row + 2, col))
      case HoverDirection.West  => Seq(Position(row, col - 1), Position(row, col - 2))
      case HoverDirection.East  => Seq(Position(row, col + 1), Position(row, col + 2))
      case _                    => Seq() // No adjacent cells for not determined

    adjacentCells.foreach { position =>
      if squareMap.contains(position) then
        Platform.runLater(() => squareMap(position).updateColor(hoverColor))
    }

  def updateGrid(grid: Grid): Unit = squareMap.foreach { case (position, square) =>
    val cellColor = grid.getCell(position) match
      case Some(_: Woods)       => Color.DarkGreen
      case Some(_: Tower)       => Color.rgb(66, 39, 3)
      case Some(_: EternalFire) => Color.Red
      case _                    => Color.White

    val tokenColor = grid.getToken(position) match
      case Some(Fire)      => Color.Orange
      case Some(Firebreak) => Color.Blue
      case _               => cellColor

    Platform.runLater(() => square.updateColor(tokenColor))
  }

package it.unibo.view.components.game.gameboard.grid

import it.unibo.model.gameboard.grid.Position
import scalafx.scene.layout.GridPane
import scala.collection.mutable

class GridInitializer(
    gridSize: Int,
    squareSize: Double,
    handleCellHover: (Int, Int, HoverDirection) => Unit,
    handleCellClick: () => Unit
):
  val squareMap: mutable.Map[Position, GridSquare] = mutable.Map()

  def initializeGridSquares(gridPane: GridPane): Unit = for {
    row <- 0 until gridSize
    col <- 0 until gridSize
  } {
    val square = GridSquare(row, col, squareSize, handleCellHover, handleCellClick)
    GridPane.setRowIndex(square.getGraphicPane, row)
    GridPane.setColumnIndex(square.getGraphicPane, col)
    gridPane.children.add(square.getGraphicPane)
    squareMap(Position(row, col)) = square
    square.toggleRectangleActivation()
  }
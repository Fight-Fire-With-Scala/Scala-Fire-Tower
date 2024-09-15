package it.unibo.view.component.game.gameboard.grid

import scala.collection.mutable

import it.unibo.model.gameboard.grid.Position
import scalafx.scene.layout.GridPane

class GridInitializer(
    gridSize: Int,
    squareSize: Double,
    handleCellHover: (Int, Int, HoverDirection) => Unit,
    handleCellClick: (Int, Int) => Unit,
):
  private val squareMap: mutable.Map[Position, GridSquare] = mutable.Map()

  def initializeGridSquares(gridPane: GridPane): mutable.Map[Position, GridSquare] =
    for {
      row <- 0 until gridSize
      col <- 0 until gridSize
    }
      val square = GridSquare(
        row,
        col,
        squareSize,
        handleCellHover,
        handleCellClick
      )
      GridPane.setRowIndex(square.getGraphicPane, row)
      GridPane.setColumnIndex(square.getGraphicPane, col)
      gridPane.children.add(square.getGraphicPane)
      squareMap(Position(row, col)) = square
    squareMap
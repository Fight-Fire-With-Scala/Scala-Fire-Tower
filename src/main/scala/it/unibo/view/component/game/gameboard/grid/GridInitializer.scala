package it.unibo.view.component.game.gameboard.grid

import scala.collection.mutable
import it.unibo.model.gameboard.grid.Position
import scalafx.scene.layout.GridPane

object GridInitializer:
  def initializeGridSquares(
      gridSize: Int,
      squareSize: Double,
      handleCellHover: (Int, Int, HoverDirection) => Unit,
      handleCellClick: (Int, Int) => Unit,
      gridPane: GridPane
  ): mutable.Map[Position, GridSquare] =
    val squareMap: mutable.Map[Position, GridSquare] = mutable.Map()
    for {
      row <- 0 until gridSize
      col <- 0 until gridSize
    } do
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

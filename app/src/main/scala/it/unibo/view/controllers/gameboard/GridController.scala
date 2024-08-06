package it.unibo.view.controllers.gameboard

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.grid.{EternalFire, Fire, Firebreak, Grid, Position, Tower, Woods}
import it.unibo.view.controllers.GraphicController
import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import scalafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import scalafx.application.Platform
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

import scala.collection.mutable
import scala.compiletime.uninitialized

class GridController(observableSubject: ViewSubject) extends GraphicController:

  @FXML
  private var container: StackPane = uninitialized
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16
  private val squareSize = 40
  private val rectangleMap: mutable.Map[(Int, Int), Rectangle] = mutable.Map()

  @FXML
  def initialize(): Unit =
    gridPane = new GridPane
    // Add squares to the GridPane
    for {
      row <- 0 until gridSize
      col <- 0 until gridSize
    } {
      val square = new Rectangle:
        width = squareSize
        height = squareSize
        fill = if ((row + col) % 2 == 0) Color.White else Color.Gray
        stroke = Color.Black
      square.onMouseMoved = (event: MouseEvent) => handleRectangleHover(event, row, col)
      GridPane.setRowIndex(square, row)
      GridPane.setColumnIndex(square, col)
      gridPane.children.add(square)
      rectangleMap((row, col)) = square
    }
    container.getChildren.add(gridPane)

  private def handleRectangleHover(event: MouseEvent, row: Int, col: Int): Unit =
    val square = rectangleMap((row, col))
    val direction = getDirection(event, square)
    println(s"Hovering over square at row $row, col $col")
    println(s"Direction: $direction")

  private def getDirection(event: MouseEvent, square: Rectangle): String =
    val x = event.getX
    val y = event.getY
    val width = square.getWidth
    val height = square.getHeight
    if (y < height / 3) "North"
    else if (y > 2 * height / 3) "South"
    else if (x < width / 3) "West"
    else if (x > 2 * width / 3) "East"
    else "Center"

  def updateGrid(grid: Grid): Unit = for {
    i <- 0 until gridSize
    j <- 0 until gridSize
  } {
    val position = Position(i, j)
    val cellColor = grid.getCell(position) match
      case Some(_: Woods)       => Color.DarkGreen
      case Some(_: Tower)       => Color.Brown
      case Some(_: EternalFire) => Color.Red
      case _                    => Color.White

    val tokenColor = grid.getToken(position) match
      case Some(Fire)      => Color.Orange
      case Some(Firebreak) => Color.Blue
      case _               => cellColor

    Platform.runLater(() => rectangleMap((i, j)).setFill(tokenColor))
  }
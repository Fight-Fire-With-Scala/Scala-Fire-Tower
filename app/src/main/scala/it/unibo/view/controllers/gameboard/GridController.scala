package it.unibo.view.controllers.gameboard

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.grid.{EternalFire, Fire, Firebreak, Grid, Position, Tower, Woods}
import it.unibo.view.controllers.GraphicController
import javafx.fxml.FXML
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
  private val rectangleMap: mutable.Map[(Int, Int), Rectangle] = mutable.Map()

  @FXML
  def initialize(): Unit =
    gridPane = new GridPane
    //Add squares to the GridPane
    for (row <- 0 until gridSize; col <- 0 until gridSize) {
      val square = new Rectangle {
        width = 30
        height = 30
        fill = if ((row + col) % 2 == 0) Color.White else Color.Gray
        stroke = Color.Black
      }
      GridPane.setRowIndex(square, row)
      GridPane.setColumnIndex(square, col)
      gridPane.children.add(square)
      rectangleMap((row, col)) = square
    }
    container.getChildren.add(gridPane)

//  private def updateSquaresSizes(): Unit = {
//    val width = container.getWidth
//    val height = container.getHeight
//    val squareSize = width / gridSize
//    print("Square size: " + squareSize)
//    for (row <- 0 until gridSize; col <- 0 until gridSize) {
//      val square = rectangleMap((row, col))
//      square.width = squareSize
//      square.height = squareSize
//    }
//  }

//  private def handleRectangleClick(row: Int, col: Int): Unit =
//    val rectangle = rectangleMap((row, col))
//    val pane = rectangle.getParent.asInstanceOf[Pane]
//    val text = new Text("!")
//    text.setFill(Color.RED)
//    text.setFont(new Font(rectangle.getHeight / 2)) // Adjust font size to fit within the rectangle
//    text.setX(rectangle.getWidth / 2 - text.getLayoutBounds.getWidth / 2)
//    text.setY(rectangle.getHeight / 2 + text.getLayoutBounds.getHeight / 6)
//    pane.getChildren.add(text)
//    println(s"Rectangle at ($row, $col) clicked")

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

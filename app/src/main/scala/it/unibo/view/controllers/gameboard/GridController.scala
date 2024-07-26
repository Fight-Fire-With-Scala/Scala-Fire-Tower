package it.unibo.view.controllers.gameboard

import it.unibo.view.controllers.GraphicController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.layout.{GridPane, Pane}
import javafx.scene.shape.Rectangle
import javafx.application.Platform
import javafx.scene.paint.Color
import javafx.scene.input.MouseEvent
import javafx.scene.text.{Font, Text}

import scala.collection.mutable

class GridController extends GraphicController {

  @FXML
  private var gridPane: GridPane = _
  private val gridSize = 16
  private val rectangleMap: mutable.Map[(Int, Int), Rectangle] = mutable.Map()

  @FXML
  def initialize(): Unit = {
    // Set fixed size constraints for the GridPane
    gridPane.setMinSize(900, 600)
    gridPane.setMaxSize(900, 600)

    // Dynamically create and add panes with rectangles to the GridPane
    for (i <- 0 until gridSize; j <- 0 until gridSize) {
      val pane = new Pane()
      val rectangle = new Rectangle()
      rectangle.setStroke(Color.BLACK)
      rectangle.setStrokeWidth(1)
      rectangle.setFill(Color.DARKCYAN)
      rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, (_: MouseEvent) => {
        handleRectangleClick(i, j)
      })
      pane.getChildren.add(rectangle)
      gridPane.add(pane, i, j)
      rectangleMap((i, j)) = rectangle
    }

    // Ensure we run updateRectangleSizes after the layout is complete
    Platform.runLater(() => {
      updateRectangleSizes()

      // Add listeners to handle resizing dynamically
      gridPane.widthProperty().addListener((_, _, _) => updateRectangleSizes())
      gridPane.heightProperty().addListener((_, _, _) => updateRectangleSizes())
    })
  }

  private def updateRectangleSizes(): Unit = {
    val numColumns = gridSize
    val numRows = gridSize
    val width = gridPane.getWidth
    val height = gridPane.getHeight

    // Calculate the minimum dimension to ensure cells are square
    val squareSize = if(width > 0) { width / numColumns } else { 0 }
    println("Resize event: width=" + width + ", height=" + height + ", squareSize=" + squareSize)
    // Resize rectangles within panes to be square and fill the entire cell
    for (i <- 0 until numColumns; j <- 0 until numRows) {
      val rectangle = rectangleMap((i, j))
      rectangle.setWidth(squareSize)
      rectangle.setHeight(squareSize)
    }
  }

  private def handleRectangleClick(row: Int, col: Int): Unit = {
    val rectangle = rectangleMap((row, col))
    val pane = rectangle.getParent.asInstanceOf[Pane]
    val text = new Text("!")
    text.setFill(Color.RED)
    text.setFont(new Font(rectangle.getHeight / 2)) // Adjust font size to fit within the rectangle
    text.setX(rectangle.getWidth / 2 - text.getLayoutBounds.getWidth / 2)
    text.setY(rectangle.getHeight / 2 + text.getLayoutBounds.getHeight / 6)
    pane.getChildren.add(text)
    println(s"Rectangle at ($row, $col) clicked")
  }
}
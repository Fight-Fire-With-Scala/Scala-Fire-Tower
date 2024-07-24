package it.unibo.view.controllers.gameboard

import it.unibo.view.controllers.GraphicController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.layout.{GridPane, Pane}
import javafx.scene.shape.Rectangle
import javafx.application.Platform
import javafx.scene.paint.Color

import scala.compiletime.uninitialized

class GridController extends GraphicController {

  @FXML
  private var gridPane: GridPane = uninitialized
  private val gridSize = 16

  @FXML
  def initialize(): Unit = {
    // Set fixed size constraints for the GridPane
    gridPane.setMinSize(900, 600)
    //gridPane.setMaxSize(900, 600)

    // Dynamically create and add panes with rectangles to the GridPane
    for (i <- 0 until gridSize; j <- 0 until gridSize) {
      val pane = new Pane()
      val rectangle = new Rectangle()
      rectangle.setStroke(Color.BLACK)
      rectangle.setStrokeWidth(1)
      rectangle.setFill(Color.LIGHTGRAY)
      pane.getChildren.add(rectangle)
      gridPane.add(pane, i, j)
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

    // Resize rectangles within panes to be square and fill the entire cell
    for (i <- 0 until numColumns; j <- 0 until numRows) {
      val pane = gridPane.getChildren.get(i * numColumns + j).asInstanceOf[Pane]
      val rectangle = pane.getChildren.get(0).asInstanceOf[Rectangle]
      rectangle.setWidth(squareSize)
      rectangle.setHeight(squareSize)
    }
  }
}
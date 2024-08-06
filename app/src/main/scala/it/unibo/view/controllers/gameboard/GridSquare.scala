package it.unibo.view.controllers.gameboard

import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import javafx.scene.input.MouseEvent

class GridSquare(val row: Int, val col: Int, val size: Double, val onHover: (Int, Int, String) => Unit):
  private val rectangle: Rectangle = new Rectangle:
    width = size
    height = size
    stroke = Color.Black
    onMouseMoved = (event: MouseEvent) => handleHover(event)

  private def handleHover(event: MouseEvent): Unit =
    val direction = getDirection(event)
    onHover(row, col, direction)

  private def getDirection(event: MouseEvent): String =
    val x = event.getX
    val y = event.getY
    val width = rectangle.getWidth
    val height = rectangle.getHeight
    if (y < height / 3) "North"
    else if (y > 2 * height / 3) "South"
    else if (x < width / 3) "West"
    else if (x > 2 * width / 3) "East"
    else "Center"

  def getGraphicRectangle: Rectangle = rectangle
  
  def updateColor(color: Color): Unit =
    rectangle.setFill(color)
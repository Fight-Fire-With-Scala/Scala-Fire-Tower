package it.unibo.view.controllers.gameboard

import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import javafx.scene.input.MouseEvent

enum HoverDirection:
  case North, South, West, East, Center

object HoverDirection:
  def fromCoordinates(x: Double, y: Double, width: Double, height: Double): HoverDirection =
    if y < height / 3 then HoverDirection.North
    else if y > 2 * height / 3 then HoverDirection.South
    else if x < width / 3 then HoverDirection.West
    else if x > 2 * width / 3 then HoverDirection.East
    else HoverDirection.Center

case class GridSquare(row: Int, col: Int, size: Double, onHover: (Int, Int, String) => Unit):
  private val rectangle: Rectangle = new Rectangle:
    width = size
    height = size
    stroke = Color.Black
    onMouseMoved = (event: MouseEvent) => handleHover(event)

  private def handleHover(event: MouseEvent): Unit =
    val direction = HoverDirection
      .fromCoordinates(event.getX, event.getY, rectangle.getWidth, rectangle.getHeight).toString
    onHover(row, col, direction)

  def getGraphicRectangle: Rectangle = rectangle

  def updateColor(color: Color): Unit = rectangle.setFill(color)

package it.unibo.view.components.gameboard

import it.unibo.model.gameboard.Direction

case class HoverDirection(direction: Direction, isDetermined: Boolean = true)

object HoverDirection:
  def fromCoordinates(x: Double, y: Double, width: Double, height: Double): HoverDirection =
    if y < height / 3 && x >= width / 3 && x <= 2 * width / 3 then HoverDirection(Direction.North)
    else if y > 2 * height / 3 && x >= width / 3 && x <= 2 * width / 3 then HoverDirection(Direction.South)
    else if x < width / 3 && y >= height / 3 && y <= 2 * height / 3 then HoverDirection(Direction.West)
    else if x > 2 * width / 3 && y >= height / 3 && y <= 2 * height / 3 then HoverDirection(Direction.East)
    else HoverDirection(Direction.North, isDetermined = false)
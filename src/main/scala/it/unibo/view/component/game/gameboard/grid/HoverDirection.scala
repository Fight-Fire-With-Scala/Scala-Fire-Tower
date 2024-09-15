package it.unibo.view.component.game.gameboard.grid

import it.unibo.model.gameboard.Direction

final case class HoverDirection(direction: Option[Direction])

object HoverDirection:
  def fromCoordinates(x: Double, y: Double, width: Double, height: Double): HoverDirection =
    val direction =
      if y < height / 3 && x >= width / 3 && x <= 2 * width / 3 then Some(Direction.North)
      else if y > 2 * height / 3 && x >= width / 3 && x <= 2 * width / 3 then Some(Direction.South)
      else if x < width / 3 && y >= height / 3 && y <= 2 * height / 3 then Some(Direction.West)
      else if x > 2 * width / 3 && y >= height / 3 && y <= 2 * height / 3 then Some(Direction.East)
      else None
    HoverDirection(direction)

package it.unibo.view.component.game.gameboard.grid

import it.unibo.model.gameboard.Direction

/**
 * The HoverDirection class represents the direction of a hover event on the grid.
 *
 * @param direction the optional direction of the hover event
 */
final case class HoverDirection(direction: Option[Direction])

object HoverDirection:
  /**
   * Determines the hover direction based on the coordinates of the hover event.
   *
   * @param x      the x-coordinate of the hover event
   * @param y      the y-coordinate of the hover event
   * @param width  the width of the grid square
   * @param height the height of the grid square
   * @return the HoverDirection based on the coordinates
   */
  def fromCoordinates(x: Double, y: Double, width: Double, height: Double): HoverDirection =
    val direction =
      if y < height / 3 && x >= width / 3 && x <= 2 * width / 3 then Some(Direction.North)
      else if y > 2 * height / 3 && x >= width / 3 && x <= 2 * width / 3 then Some(Direction.South)
      else if x < width / 3 && y >= height / 3 && y <= 2 * height / 3 then Some(Direction.West)
      else if x > 2 * width / 3 && y >= height / 3 && y <= 2 * height / 3 then Some(Direction.East)
      else None
    HoverDirection(direction)

package it.unibo.view.controllers.gameboard

enum HoverDirection:
  case North, South, West, East, NotDetermined

object HoverDirection:
  def fromCoordinates(x: Double, y: Double, width: Double, height: Double): HoverDirection =
    if y < height / 3 && x >= width / 3 && x <= 2 * width / 3 then HoverDirection.North
    else if y > 2 * height / 3 && x >= width / 3 && x <= 2 * width / 3 then HoverDirection.South
    else if x < width / 3 && y >= height / 3 && y <= 2 * height / 3 then HoverDirection.West
    else if x > 2 * width / 3 && y >= height / 3 && y <= 2 * height / 3 then HoverDirection.East
    else HoverDirection.NotDetermined
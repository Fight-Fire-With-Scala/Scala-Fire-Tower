package it.unibo.model.gameboard.grid

import scala.annotation.targetName

case class Position(x: Int, y: Int):
  @targetName("plus")
  def +(other: Position): Position =
    Position(this.x + other.x, this.y + other.y)
    
  @targetName("minus")
  def -(other: Position): Position =
    Position(this.x - other.x, this.y - other.y) 

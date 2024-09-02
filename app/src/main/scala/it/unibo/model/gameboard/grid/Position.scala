package it.unibo.model.gameboard.grid

import scala.annotation.targetName

final case class Position(row: Int, col: Int):
  @targetName("plus")
  def +(other: Position): Position =
    Position(this.row + other.row, this.col + other.col)
    
  @targetName("minus")
  def -(other: Position): Position =
    Position(this.row - other.row, this.col - other.col)

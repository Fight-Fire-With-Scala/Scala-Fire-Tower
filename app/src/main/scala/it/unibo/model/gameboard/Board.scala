package it.unibo.model.gameboard

import it.unibo.model.gameboard.grid.Grid

import scala.util.Random

final case class Board(grid: Grid, windDirection: Direction):
  override def toString: String =
    s"Board:\nGrid:\n${grid.toString}\nWind Direction: ${windDirection.toString}"

object Board:
  def withRandomWindAndStandardGrid: Board =
    val randomWindDirection = Random.shuffle(Direction.values).head
    Board(Grid.standard, randomWindDirection)

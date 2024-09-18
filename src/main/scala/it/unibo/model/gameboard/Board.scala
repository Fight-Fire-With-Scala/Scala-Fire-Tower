package it.unibo.model.gameboard

import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import scala.util.Random
import it.unibo.model.gameboard.grid.{ Grid, TowerPosition }

final case class Board(grid: Grid, windDirection: Direction):
  override def toString: String =
    s"Board:\nGrid:\n${grid.toString}\nWind Direction: ${windDirection.toString}"

  def isOnFire(towerPosition: TowerPosition): Boolean =
    grid.getToken(towerPosition.position).contains(Fire)

object Board:
  def withRandomWindAndStandardGrid: Board =
    val randomWindDirection = Random.shuffle(Direction.values).head
    Board(Grid.standard, randomWindDirection)

  def withRandomWindAndEndgameGrid: Board =
    val randomWindDirection = Random.shuffle(Direction.values).head
    Board(Grid.endGame, randomWindDirection)

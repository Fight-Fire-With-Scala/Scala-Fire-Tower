package it.unibo.model.gameboard.board

import it.unibo.model.cards.effects.{GameEffect, PatternComputationEffect, WindEffect}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Grid

import scala.util.Random

case class Board(grid: Grid, windDirection: Direction):
  private def updateWind(newWindEffect: WindEffect): Board =
    copy(windDirection = newWindEffect.direction)
  def applyEffect(gameEffect: Option[GameEffect]): Board = gameEffect match
    case Some(wd: WindEffect)              => updateWind(wd)
    // Is not said that the pattern will be applied to the grid because it could be invalid.
    case Some(p: PatternComputationEffect) => this
    case Some(_)                           => this
    case None                              => this

  override def toString: String =
    s"Board:\nGrid:\n${grid.toString}\nWind Direction: ${windDirection.toString}"

object Board:
  def withRandomWindAndStandardGrid: Board =
    val randomWindDirection = Random.shuffle(Direction.values).head
    Board(Grid.standard, randomWindDirection)
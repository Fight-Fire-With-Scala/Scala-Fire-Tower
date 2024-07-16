package it.unibo.model.board

import it.unibo.model.cards.GameEffect
import it.unibo.model.cards.resolvers.tokens.PatternToApply
import it.unibo.model.grid.Grid
import it.unibo.model.cards.resolvers.wind.WindDirection

import scala.util.Random

case class Board(grid: Grid, windDirection: WindDirection) {
  def updateWind(newWindDirection: WindDirection): Board = copy(windDirection = newWindDirection)
  def applyEffect(gameEffect: Option[GameEffect]): Board = gameEffect match
    case Some(wd: WindDirection) => updateWind(wd)
    //Is not said that the pattern will be applied to the grid because it could be invalid.
    case Some(p: PatternToApply) => ???
    case Some(_) => this
    case None => this
}

object Board {
  def withRandomWindAndStandardGrid: Board = {
    val windDirections = Seq(WindDirection.North, WindDirection.South, WindDirection.East, WindDirection.West)
    val randomWindDirection = windDirections(Random.nextInt(windDirections.length))
    Board(Grid.standard, randomWindDirection)
  }
}
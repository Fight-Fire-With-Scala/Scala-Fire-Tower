package it.unibo.model.board

import it.unibo.model.grid.Grid
import it.unibo.model.cards.resolvers.wind.WindDirection

import scala.util.Random

case class Board(grid: Grid, windDirection: WindDirection) {
  def updateWind(newWindDirection: WindDirection): Board = copy(windDirection = newWindDirection)

  //def resolveGameEffect
}

object Board {
  def withRandomWindAndStandardGrid: Board = {
    val windDirections = Seq(WindDirection.North, WindDirection.South, WindDirection.East, WindDirection.West)
    val randomWindDirection = windDirections(Random.nextInt(windDirections.length))
    Board(Grid.standard, randomWindDirection)
  }

}
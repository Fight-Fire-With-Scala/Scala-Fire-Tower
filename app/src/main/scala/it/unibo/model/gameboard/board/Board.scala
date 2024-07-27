package it.unibo.model.gameboard.board

import it.unibo.model.cards.effects.{GameEffect, PatternComputationEffect, WindEffect}
import it.unibo.model.gameboard.grid.Grid

import scala.util.Random

case class Board(grid: Grid, windDirection: WindEffect):
  private def updateWind(newWindDirection: WindEffect): Board =
    copy(windDirection = newWindDirection)
  def applyEffect(gameEffect: Option[GameEffect]): Board = gameEffect match
    case Some(wd: WindEffect)              => updateWind(wd)
    // Is not said that the pattern will be applied to the grid because it could be invalid.
    case Some(p: PatternComputationEffect) => this
    case Some(_)                           => this
    case None                              => this

object Board:
  def withRandomWindAndStandardGrid: Board =
    val randomWindDirection = Random.shuffle(WindEffect.values).head
    Board(Grid.standard, randomWindDirection)

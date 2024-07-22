package it.unibo.model.board

import it.unibo.model.cards.effects.{CardEffect, PatternComputationEffect, WindEffect}
import it.unibo.model.grid.Grid

import scala.util.Random

case class Board(grid: Grid, windDirection: WindEffect):
  private def updateWind(newWindDirection: WindEffect): Board =
    copy(windDirection = newWindDirection)
  def applyEffect(gameEffect: Option[CardEffect]): Board = gameEffect match
    case Some(wd: WindEffect)              => updateWind(wd)
    // Is not said that the pattern will be applied to the grid because it could be invalid.
    case Some(p: PatternComputationEffect) => ???
    case Some(_)                           => this
    case None                              => this

object Board:
  def withRandomWindAndStandardGrid: Board =
    val randomWindDirection = Random.shuffle(WindEffect.windDirections).head
    Board(Grid.standard, randomWindDirection)

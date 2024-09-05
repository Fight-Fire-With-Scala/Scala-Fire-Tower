package it.unibo.model.gameboard.board

import it.unibo.model.cards.effects.{
  GameEffect,
  PatternChoiceEffect,
  PatternComputationEffect,
  WindEffect
}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{Grid, Position, Token}

import scala.util.Random

final case class Board(
    grid: Grid,
    currentCardId: Option[Int],
    availablePatterns: Set[Map[Position, Token]],
    windDirection: Direction
):
  private def updateWind(newWindEffect: WindEffect): Board =
    copy(windDirection = newWindEffect.direction)

  private def applyPatternChoice(p: PatternChoiceEffect): Board =
    copy(grid = grid.setTokens(p.pattern.toSeq*))

  def applyEffect(gameEffect: Option[GameEffect]): Board = gameEffect match
    case Some(wd: WindEffect)              => updateWind(wd)
    // Is not said that the pattern will be applied to the grid because it could be invalid.
    case Some(p: PatternComputationEffect) => copy(availablePatterns = p.patterns)
    case Some(p: PatternChoiceEffect)      => applyPatternChoice(p)
    case Some(_)                           => this
    case None                              => this

  override def toString: String =
    s"Board:\nGrid:\n${grid.toString}\nWind Direction: ${windDirection.toString}"

object Board:
  def withRandomWindAndStandardGrid: Board =
    val randomWindDirection = Random.shuffle(Direction.values).head
    Board(Grid.standard, None, Set.empty, randomWindDirection)

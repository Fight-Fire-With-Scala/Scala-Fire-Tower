package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.{BoardEffect, PatternChoiceEffect, PatternComputationEffect}
import it.unibo.model.gameboard.board.Board

trait EffectResolver

sealed trait StepResolver extends EffectResolver

sealed case class PatternComputationResolver(pattern: PatternChoiceEffect) extends StepResolver:
  def getAvailableMoves: Board => PatternComputationEffect =
    (b: Board) => PrologSolver().solveWithProlog(b, pattern)

sealed case class PatternApplicationResolver(pattern: PatternChoiceEffect) extends StepResolver:
  def applyMove: Board => BoardEffect = (b: Board) => BoardEffect(b.applyEffect(Some(pattern)))

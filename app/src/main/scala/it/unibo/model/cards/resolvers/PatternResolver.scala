package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.{BoardEffect, CardEffect, PatternChoiceEffect}
import it.unibo.model.gameboard.board.Board

case class PatternComputationResolver(pattern: PatternChoiceEffect) extends StepResolver:
  def getAvailableMoves: Board => CardEffect =
    (b: Board) => PrologSolver().solveWithProlog(b, pattern)

case class PatternApplicationResolver() extends StepResolver:
  def applyMove: (Board, PatternChoiceEffect) => BoardEffect =
    (b: Board, p: PatternChoiceEffect) => BoardEffect(b.applyEffect(Some(p)))

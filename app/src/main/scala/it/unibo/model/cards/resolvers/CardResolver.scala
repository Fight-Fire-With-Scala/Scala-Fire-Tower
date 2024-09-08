package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.{
  BoardEffect,
  PatternChoiceEffect,
  PatternComputationEffect,
  PatternEffect
}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.ConcreteToken
import it.unibo.model.prolog.{BoardTheory, PrologEngine, Rule}
import it.unibo.model.prolog.PrologProgram.{cardsProgram, solverProgram}
import it.unibo.model.prolog.PrologUtils.given

import scala.jdk.CollectionConverters.*

trait EffectResolver

sealed trait StepResolver extends EffectResolver

sealed case class PatternComputationResolver(
    pattern: PatternEffect,
    goals: List[Rule],
    directions: List[Direction]
) extends StepResolver:
  def getAvailableMoves: Board => PatternComputationEffect = (b: Board) =>
    val theory = BoardTheory(b.grid, pattern.compilePattern, directions)
    theory.append(cardsProgram)
    theory.append(solverProgram)
    val engine = PrologEngine(theory)
    val computedPatterns = goals.map(g => engine.solveAsPatterns(g)).reduce((a, b) => a.union(b))
    val res = computedPatterns.map(m => m.filter((_, tkn) => tkn != ConcreteToken.Empty))
    PatternComputationEffect(res)

sealed case class PatternApplicationResolver(pattern: PatternChoiceEffect) extends StepResolver:
  def applyMove: Board => BoardEffect = (b: Board) => BoardEffect(b.applyEffect(Some(pattern)))

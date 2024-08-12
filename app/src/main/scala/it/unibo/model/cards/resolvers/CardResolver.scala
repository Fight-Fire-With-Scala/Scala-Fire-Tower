package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.{BoardEffect, PatternComputationEffect, PatternEffect}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.board.Board
import it.unibo.model.prolog.{BoardTheory, PrologEngine, Rule}
import it.unibo.model.prolog.PrologProgram.{cardsProgram, solverProgram}
import it.unibo.model.prolog.PrologUtils.parseComputedPatterns

import scala.jdk.CollectionConverters.*

trait EffectResolver

sealed trait StepResolver extends EffectResolver

sealed case class PatternComputationResolver(
    pattern: PatternEffect,
    goal: Rule,
    directions: List[Direction]
) extends StepResolver:
  def getAvailableMoves: Board => PatternComputationEffect = (b: Board) =>
    val theory = BoardTheory(b.grid, pattern.compilePattern, directions)
    theory.append(cardsProgram)
    theory.append(solverProgram)
    val engine = PrologEngine(theory)
    val computedPatterns = engine.solve(goal.rule).map(i => parseComputedPatterns(i)).toList
    PatternComputationEffect(computedPatterns)

sealed case class PatternApplicationResolver(pattern: PatternEffect) extends StepResolver:
  def applyMove: Board => BoardEffect = (b: Board) => BoardEffect(b.applyEffect(Some(pattern)))

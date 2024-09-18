package it.unibo.model.prolog

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import alice.tuprolog.{Term, Theory}
import it.unibo.model.effect.core.{ICardEffect, ILogicComputation, ILogicEffect, PatternLogicEffect}
import it.unibo.model.gameboard.{Direction, Pattern}
import it.unibo.model.gameboard.grid.{BasicGrid, Cell, Grid, Position, Token}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.prolog.PrologUtils.given_Conversion_SolverType_Theory
import it.unibo.model.prolog.PrologUtils.given_Conversion_Rule_Term

abstract class AbstractCardSolverSpec extends AnyWordSpecLike with Matchers:

  protected val defaultTokens: Pattern = Map.empty
  private val dummyCardId              = 1
  private val dummyEffectId            = 0

  private val cells: Map[Position, Cell] = Map(
    Position(0, 0) -> Tower,
    Position(0, 1) -> Woods,
    Position(0, 2) -> Woods,
    Position(0, 3) -> Woods,
    Position(0, 4) -> Tower,
    Position(1, 0) -> Woods,
    Position(1, 1) -> Woods,
    Position(1, 2) -> Woods,
    Position(1, 3) -> Woods,
    Position(1, 4) -> Woods,
    Position(2, 0) -> Woods,
    Position(2, 1) -> Woods,
    Position(2, 2) -> EternalFire,
    Position(2, 3) -> Woods,
    Position(2, 4) -> Woods,
    Position(3, 0) -> Woods,
    Position(3, 1) -> Woods,
    Position(3, 2) -> Woods,
    Position(3, 3) -> Woods,
    Position(3, 4) -> Woods,
    Position(4, 0) -> Tower,
    Position(4, 1) -> Woods,
    Position(4, 2) -> Woods,
    Position(4, 3) -> Woods,
    Position(4, 4) -> Tower
  )

  private def buildTheory(
      patternsToCompute: Map[Option[Int], List[ILogicEffect]],
      tokens: Pattern = defaultTokens
  ): Theory =
    val b = GridTheory(BasicGrid(cells, tokens), patternsToCompute)
    b.append(SolverType.CardSolver)
    b.append(SolverType.BaseSolver)
    b

  private def buildEngine(
      patternsToCompute: Map[Option[Int], List[ILogicEffect]],
      tokens: Pattern = defaultTokens
  ): PrologEngine = PrologEngine(buildTheory(patternsToCompute, tokens))

  protected def getAvailablePatterns(
      logicComputation: ILogicComputation
  ): Set[Pattern] =
    val engine = buildEngine(
      Map(Some(dummyCardId) -> List(PatternLogicEffect(List(logicComputation))))
    )
    engine.solveAsPatterns(logicComputation.goal(Some(dummyCardId), Some(dummyEffectId)))

object AbstractCardSolverSpec:
  given Conversion[ICardEffect, ILogicComputation] = _.computations.head

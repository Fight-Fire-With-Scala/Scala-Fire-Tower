package it.unibo.model.prolog

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import alice.tuprolog.{SolveInfo, Term, Theory}
import it.unibo.model.prolog.PrologSolver.{engine, solverProgram}
import Scala2P.{solveWithSuccess, given}

abstract class AbstractRuleSpec extends AnyWordSpecLike with Matchers:
  protected val board: String = """
      |    cell((0, 0), t).
      |    cell((0, 1), t).
      |    cell((0, 2), w).
      |    cell((0, 3), w).
      |    cell((0, 4), t).
      |    cell((1, 0), w).
      |    cell((1, 1), w).
      |    cell((1, 2), w).
      |    cell((1, 3), w).
      |    cell((1, 4), w).
      |    cell((2, 0), w).
      |    cell((2, 1), w).
      |    cell((2, 2), ef).
      |    cell((2, 3), w).
      |    cell((2, 4), w).
      |    cell((3, 0), w).
      |    cell((3, 1), w).
      |    cell((3, 2), w).
      |    cell((3, 3), w).
      |    cell((3, 4), w).
      |    cell((4, 0), t).
      |    cell((4, 1), w).
      |    cell((4, 2), w).
      |    cell((4, 3), w).
      |    cell((4, 4), t).
      |
      |    token((0, 2), f).
      |    token((0, 3), f).
      |    token((0, 4), f).
      |
      |    pattern((0, 0), f).
      |    pattern((0, 1), f).
      |
      |    directions([north, west, east, south]).
      |    deltas([(1, 0), (-1, 0), (0, 1), (0, -1)]).
      |
      |    gridSize(5).
      |""".stripMargin
  
  protected val theory: Theory = board + solverProgram
  protected val checkSolverResult: Term => Boolean = solveWithSuccess(theory, engine)
  protected val solver: Term => Theory => LazyList[SolveInfo] = engine

  protected val dummyPattern = "[(0, 2, f), (0, 3, f)]"
  protected val dummyTrueRequiredCells = "[w]"
  protected val dummyTrueRequiredTokens = "[f]"
  protected val dummyFalseRequiredCells = "[ef]"
  protected val dummyFalseRequiredTokens = "[k]"
  protected val dummyPosition = "(0, 2)"

  given Conversion[Rule, Term] = _.toTerm

package it.unibo.model.reasoner

import alice.tuprolog.Prolog
import alice.tuprolog.SolveInfo
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.reasoner.ReasonerEngine.engineWithSolver
import it.unibo.model.reasoner.ReasonerEngine.engineWithTheory
import it.unibo.model.reasoner.ReasonerUtils.parseComputedPatterns
import it.unibo.model.gameboard.Pattern

final case class ReasonerEngine(theory: Theory):
  private val engine = engineWithSolver(engineWithTheory(theory))

  def solve(goal: Term): LazyList[SolveInfo] = engine(goal)

  def solveAsPatterns(goal: Term): Set[Pattern] =
    solve(goal).map(i => parseComputedPatterns(i)).map(_.toSet).distinct.map(_.toMap).toSet

  def isSolvedWithSuccess(goal: Term): Boolean =
    engine(goal).map(_.isSuccess).headOption.contains(true)

object ReasonerEngine:
  private val engine: Prolog = Prolog()

  private val engineWithTheory: Theory => Prolog = t =>
    engine.setTheory(t)
    engine

  private val engineWithSolver: Prolog => Term => LazyList[SolveInfo] = engine =>
    goal =>
      new Iterable[SolveInfo]:
        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo]:
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean = solution.isDefined &&
            (solution.get.isSuccess || solution.get.hasOpenAlternatives)

          override def next(): SolveInfo =
            try solution.get
            finally
              solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
      .to(LazyList)

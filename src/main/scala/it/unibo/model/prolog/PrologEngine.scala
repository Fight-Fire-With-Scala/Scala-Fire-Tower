package it.unibo.model.prolog

import alice.tuprolog.Prolog
import alice.tuprolog.SolveInfo
import alice.tuprolog.Term
import alice.tuprolog.Theory
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.PrologEngine.engineWithResolver
import it.unibo.model.prolog.PrologEngine.engineWithTheory
import it.unibo.model.prolog.PrologUtils.parseComputedPatterns

final case class PrologEngine(theory: Theory):
  private val engine = engineWithResolver(engineWithTheory(theory))

  def solve(goal: Term): LazyList[SolveInfo] = engine(goal)
  
  def solveAsPatterns(goal: Term): Set[Map[Position, Token]] =
    solve(goal).map(i => parseComputedPatterns(i)).map(_.toSet).distinct.map(_.toMap).toSet
  
  def isSolvedWithSuccess(goal: Term): Boolean = 
    engine(goal).map(_.isSuccess).headOption.contains(true)

object PrologEngine:
  private val engine: Prolog = Prolog()

  private val engineWithTheory: Theory => Prolog = t =>
    engine.setTheory(t)
    engine

  private val engineWithResolver: Prolog => Term => LazyList[SolveInfo] = engine =>
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

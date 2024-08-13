package it.unibo.model.prolog

import alice.tuprolog.{Prolog, SolveInfo, Term, Theory}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.prolog.PrologEngine.{engineWithResolver, engineWithTheory}
import it.unibo.model.prolog.PrologUtils.parseComputedPatterns

final case class PrologEngine(private val theory: Theory):
  private val engine = engineWithResolver(engineWithTheory(theory))

  def solve(goal: Term): LazyList[SolveInfo] = engine(goal)

  def solveAsPatternList(goal: Term): List[Map[Position, Token]] =
    solve(goal).map(i => parseComputedPatterns(i)).toList
  
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

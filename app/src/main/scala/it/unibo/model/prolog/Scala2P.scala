package it.unibo.model.prolog

import alice.tuprolog.*

object Scala2P:
  def extractTerm(solveInfo: SolveInfo, i: Integer): Term = solveInfo.getSolution
    .asInstanceOf[Struct].getArg(i).getTerm

  private def extractTerm(solveInfo: SolveInfo, s: String): Term = solveInfo.getTerm(s)

  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[?], Term] = _.mkString("[", ",", "]")
  given Conversion[String, Theory] = new Theory(_)

  def mkPrologEngine(goal: Term): Theory => LazyList[SolveInfo] = t =>
    val engine = Prolog()
    engine.setTheory(t)
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

  def solveWithSuccess(theory: Theory, solver: Term => Theory => LazyList[SolveInfo])(goal: Term): Boolean =
    solver(goal)(theory).map(_.isSuccess).headOption.contains(true)

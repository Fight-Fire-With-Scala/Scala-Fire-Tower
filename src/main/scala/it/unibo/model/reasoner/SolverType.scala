package it.unibo.model.reasoner

import alice.tuprolog.Theory
import it.unibo.model.reasoner.SolverType.getClass

enum SolverType(val prologSourcePath: String):
  case BaseSolver extends SolverType("/prolog/solver.pl")
  case CardSolver extends SolverType("/prolog/cards.pl")
  case DistanceSolver extends SolverType("/prolog/distance.pl")
  case ManhattanSolver extends SolverType("/prolog/manhattan.pl")
  case CardChoserSolver extends SolverType("/prolog/choseCard.pl")
  case ConcatListSolver extends SolverType("/prolog/concatLists.pl")

  def getTheory(path: String): Theory = Theory.parseWithStandardOperators(parseTheory(path))

  private val parseTheory: String => java.io.InputStream =
    path => getClass.getResourceAsStream(path)

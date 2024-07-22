package it.unibo.model.cards.resolvers

import it.unibo.model.board.Board
import it.unibo.model.cards.effects.{PatternChoiceEffect, PatternComputationEffect}
//import it.unibo.prolog.ExampleKt.searchValidPatterns
//import it.unibo.tuprolog.core.Term

case class PrologSolver():
  def solveWithProlog(b: Board, p: PatternChoiceEffect): PatternComputationEffect = ???
//    val res: java.util.List[Term] = searchValidPatterns()
//    res.forEach(println(_))

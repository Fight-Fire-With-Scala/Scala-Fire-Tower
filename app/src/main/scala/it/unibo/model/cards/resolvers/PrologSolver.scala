package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.{PatternChoiceEffect, PatternComputationEffect}
import it.unibo.model.gameboard.board.Board
//import it.unibo.prolog.ExampleKt.searchValidPatterns
//import it.unibo.tuprolog.core.Term

case class PrologSolver():
  def solveWithProlog(b: Board, p: PatternChoiceEffect): PatternComputationEffect =
    PatternComputationEffect(List.empty)
//    val res: java.util.List[Term] = searchValidPatterns()
//    res.forEach(println(_))

package it.unibo.model.cards.resolvers

import it.unibo.model.cards.effects.{PatternChoiceEffect, PatternComputationEffect}
import it.unibo.model.gameboard.board.Board
import it.unibo.prolog.MainKt.test
//import it.unibo.tuprolog.core.Term

case class PrologSolver():
  def solveWithProlog(): Unit = println(test())

  def solveWithProlog(b: Board, p: PatternChoiceEffect): PatternComputationEffect =
    PatternComputationEffect(List.empty)
//    val res: java.util.List[Term] = searchValidPatterns()
//    res.forEach(println(_))

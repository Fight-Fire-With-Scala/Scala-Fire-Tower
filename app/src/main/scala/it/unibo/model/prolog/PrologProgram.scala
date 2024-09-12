package it.unibo.model.prolog

import alice.tuprolog.Theory

object PrologProgram:

  private val parseTheory: String => java.io.InputStream =
    path => getClass.getResourceAsStream(path)

  val solverProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/solver.pl"))

  val cardsProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/cards.pl"))
  
  val distanceProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/distance.pl"))

  val manhattanDistance: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/manhattan.pl"))
  
  val choseCardProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/choseCard.pl"))
  
  val concatListsProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/concatLists.pl"))

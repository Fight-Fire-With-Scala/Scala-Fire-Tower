package it.unibo.model.prolog

import alice.tuprolog.Theory

object PrologProgram:

  private val parseTheory: String => java.io.InputStream =
    path => getClass.getResourceAsStream(path)

  val solverProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/solver.pl"))

  val cardsProgram: Theory = Theory.parseWithStandardOperators(parseTheory("/prolog/cards.pl"))

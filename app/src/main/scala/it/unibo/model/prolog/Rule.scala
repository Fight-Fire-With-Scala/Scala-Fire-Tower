package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.model.prolog.PrologUtils.given_Conversion_Int_Term

final case class Rule(term: Struct)

object Rule:
  def apply(ruleName: String): Int => Rule =
    cardId => Rule(Struct.of(ruleName, Var.anonymous(), cardId))

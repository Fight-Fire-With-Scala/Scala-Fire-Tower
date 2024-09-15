package it.unibo.model.prolog

import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Var
import it.unibo.model.prolog.PrologUtils.given_Conversion_Int_Term

final case class Rule(term: Struct)

object Rule:
  def apply(ruleName: String): (Int, Int) => Rule =
    (cardId, compId) => Rule(Struct.of(ruleName, Var.anonymous(), cardId, compId))

package it.unibo.model.prolog

import alice.tuprolog.Struct
import alice.tuprolog.Term
import alice.tuprolog.Var
import it.unibo.model.prolog.PrologUtils.given_Conversion_Int_Term

final case class Rule(term: Struct)

object Rule:
  def apply(ruleName: String): (Option[Int], Option[Int]) => Rule =
    (cardId, compId) =>
      cardId match
        case Some(id) =>
          compId match
            case Some(cId) => Rule(Struct.of(ruleName, Var.anonymous(), id, cId))
            case None      => Rule(Struct.of(ruleName, Var.anonymous(), id, Var.anonymous()))
        case None => Rule(Struct.of(ruleName, Var.anonymous(), Var.underscore(), Var.underscore()))

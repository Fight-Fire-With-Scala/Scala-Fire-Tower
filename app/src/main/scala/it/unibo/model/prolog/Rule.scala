package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Var}

case class Rule(term: Struct)

object Rule:
  def apply(ruleName: String): Rule = Rule(Struct.of(ruleName, Var.anonymous()))
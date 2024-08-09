package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Var}
import it.unibo.model.prolog.PrologUtils.given_Conversion_String_Term

sealed trait Rule:
  def toTerm: Term

final case class PointsOfInterest(allowedCells: String, allowedTokens: String)
    extends Rule:
  override def toTerm: Term = Struct.of("points_of_interest", Var.anonymous(), allowedCells, allowedTokens)

final case class PatternFit(position: String) extends Rule:
  override def toTerm: Term = Struct.of("pattern_fit", Term.createTerm(position), Var.anonymous())

final case class AtLeast(kind: String, entity: String, patternList: String, reqList: String)
    extends Rule:
  override def toTerm: Term = Struct.of("at_least", kind, entity, patternList, reqList)

final case class All(entity: String, patternList: String, reqList: String) extends Rule:
  override def toTerm: Term = Struct.of("all", entity, patternList, reqList)

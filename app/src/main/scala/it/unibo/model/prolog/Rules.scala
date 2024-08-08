package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Var}

sealed trait Rule:
  def toTerm: Term
  
final case class PointsOfInterest(position: String, allowedCells: String, allowedTokens: String) extends Rule:
  override def toTerm: Term = Term.createTerm(s"points_of_interest($position, $allowedCells, $allowedTokens)")

final case class PatternFit(position: String) extends Rule:
  override def toTerm: Term = new Struct("pattern_fit", Term.createTerm(position), Var())// Term.createTerm(s"pattern_fit($position, Results)")

final case class AtLeast(kind: String, entity: String, patternList: String, reqList: String) extends Rule:
  override def toTerm: Term = Term.createTerm(s"at_least($kind, $entity, $patternList,$reqList)")

final case class All(entity: String, patternList: String, reqList: String) extends Rule:
  override def toTerm: Term = Term.createTerm(s"all($entity, $patternList,$reqList)")
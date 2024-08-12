package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term, Var}

case class Rule(rule: Struct)

//
//final case class MatchIfBothTokenAndCell(
//    coords: String,
//    allowedCells: String,
//    allowedTokens: String
//) extends Rule:
//  override def toTerm: Term = Struct
//    .of("match_if_both_token_and_cell", Var.of(coords), allowedCells, allowedTokens)
//
//final case class MatchEmptyCells(coords: String, allowedCells: String, allowedTokens: String)
//    extends Rule:
//  override def toTerm: Term = Struct
//    .of("match_empty_cells", Var.of(coords), allowedCells, allowedTokens)
//
//final case class DisallowNeighbors(coords: String, disallowedNeighbors: String) extends Rule:
//  override def toTerm: Term = Struct.of("disallow_neighbors", Var.of(coords), disallowedNeighbors)
//
//final case class All(entity: String, patternList: String, reqList: String) extends Rule:
//  override def toTerm: Term = Struct.of("all", entity, patternList, reqList)
//
//final case class AtLeast(
//    kind: String,
//    patternList: String,
//    allowedPattern: String,
//    requiredCells: String,
//    requiredTokens: String
//) extends Rule:
//  override def toTerm: Term = Struct
//    .of("at_least", kind, patternList, allowedPattern, requiredCells, requiredTokens)
//
//final case class ComputePattern(coords: String, results: String, allowedTokens: String)
//    extends Rule:
//  override def toTerm: Term = Struct
//    .of("compute_pattern", Var.of(coords), Var.of(results), allowedTokens)
//
//final case class ComputePatternWithOffset(
//    offset: String,
//    coords: String,
//    appliedPattern: String,
//    allowedTokens: String
//) extends Rule:
//  override def toTerm: Term = Struct
//    .of("compute_pattern_with_offset", offset, coords, Var.of(appliedPattern), allowedTokens)
//
//final case class IsPatternWithoutEffect(patternList: String, kind: String) extends Rule:
//  override def toTerm: Term = Struct.of("is_pattern_without_effect", Var.of(patternList), kind)
//
//sealed trait CompositeRule extends Rule
//
////final case class AtLeastAFireLikeNeighbor() extends CompositeRule:
////  override def toTerm: Term =
////    val a = AtLeast("neigh", "R", "_", "[ef]", "[]")
////    val b = AtLeast()
////    Struct.of("(", Struct.of("->", ), "-> true ;", b.toTerm, ")")
//
//final case class Not(rule: Rule) extends CompositeRule:
//  override def toTerm: Term = Struct.of("\\+", rule.toTerm)
//
//final case class Or(firstRule: Rule, secondRule: Rule) extends CompositeRule:
//  override def toTerm: Term = Struct.of(";", firstRule.toTerm, secondRule.toTerm)

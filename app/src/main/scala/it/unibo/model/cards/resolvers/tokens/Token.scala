package it.unibo.model.cards.resolvers.tokens

trait Token

object Token:
  val tokens: Set[Token] = Set(Firebreak, Fire, Water, Reforest)

  case object Firebreak extends Token
  case object Fire extends Token
  case object Water extends Token
  case object Reforest extends Token

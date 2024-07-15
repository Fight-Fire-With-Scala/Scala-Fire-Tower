package it.unibo.model.cards.resolvers.tokens

trait Token

object Token:
  val tokens: Set[Token] = Set(Firebreak, Fire)

  case object Firebreak extends Token
  case object Fire extends Token

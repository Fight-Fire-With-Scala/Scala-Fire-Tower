package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.cards.resolvers.GameEffect

trait Token extends GameEffect

object Token:
  val tokens: Set[Token] = Set(Firebreak, Fire)

  case object Firebreak extends Token
  case object Fire extends Token

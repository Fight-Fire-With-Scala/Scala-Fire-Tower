package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.Position
import it.unibo.model.cards.GameEffect

case class PatternToApply(patterns: List[Map[Position, Token]]) extends GameEffect

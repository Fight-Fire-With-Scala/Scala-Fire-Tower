package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.cards.GameEffect
import it.unibo.model.cards.effects.Token
import it.unibo.model.grid.Position

case class PatternToApply(patterns: List[Map[Position, Token]]) extends GameEffect

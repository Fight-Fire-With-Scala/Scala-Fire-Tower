package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.AppliedSpatialPattern
import it.unibo.model.cards.GameEffect

case class PatternToApply(patterns: List[AppliedSpatialPattern]) extends GameEffect

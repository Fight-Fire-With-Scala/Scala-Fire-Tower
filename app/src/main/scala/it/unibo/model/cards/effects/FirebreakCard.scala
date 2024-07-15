package it.unibo.model.cards.effects

import it.unibo.model.cards.effects.patterns.{b, e, pattern}
import it.unibo.model.cards.resolvers.tokens.TokenResolver
import it.unibo.model.cards.resolvers.SpatialResolver
import it.unibo.model.cards.effects.HasSpatialEffect

trait FirebreakCard extends HasSpatialEffect

// noinspection DuplicatedCode
case object FirebreakCard:
  val firebreakCards: Set[FirebreakCard] = Set(DeReforest, ScratchLine, DozerLine)

  case object DeReforest extends FirebreakCard:
    val effectCode: Int = 10
    val effect: SpatialResolver = TokenResolver(pattern(b).toMatrix(1, 1))

  case object ScratchLine extends FirebreakCard:
    val effectCode: Int = 9
    val effect: SpatialResolver = TokenResolver(pattern { b; e; b }.toMatrix(1, 3))

  case object DozerLine extends FirebreakCard:
    val effectCode: Int = 8
    val effect: SpatialResolver = TokenResolver(pattern { b; b }.toMatrix(1, 2))
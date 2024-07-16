package it.unibo.model.cards.effects

import it.unibo.model.cards.effects.patterns.{b, e, pattern}
import it.unibo.model.cards.resolvers.tokens.{PatternChoice, PatternResolver, PatternResolverWithChoice, PatternToApply}
import it.unibo.model.cards.resolvers.{ResolverWithChoice, SpatialResolver}

trait FirebreakCard extends HasEffect

// noinspection DuplicatedCode
case object FirebreakCard:
  val firebreakCards: Set[FirebreakCard] = Set(DeReforest, ScratchLine, DozerLine)

  case object DeReforest extends FirebreakCard:
    val effectCode: Int = 10
    val effect: ResolverWithChoice[PatternChoice, PatternToApply] =
      PatternResolverWithChoice(pattern(b))

  case object ScratchLine extends FirebreakCard:
    val effectCode: Int = 9
    val effect: SpatialResolver = PatternResolver(pattern { b; e; b })

  case object DozerLine extends FirebreakCard:
    val effectCode: Int = 8
    val effect: SpatialResolver = PatternResolver(pattern { b; b })

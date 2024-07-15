package it.unibo.model.cards.effects

import it.unibo.model.cards.effects.patterns.{b, f, pattern}
import it.unibo.model.cards.resolvers.tokens.TokenResolver
import it.unibo.model.cards.resolvers.SpatialResolver
import it.unibo.model.cards.effects.HasSpatialEffect

trait FireCard extends HasSpatialEffect

// noinspection DuplicatedCode
case object FireCard:
  val fireCards: Set[FireCard] = Set(Explosion, Flare, BurningSnag, Ember)

  case object Explosion extends FireCard:
    val effectCode: Int = 0
    val effect: SpatialResolver =
      TokenResolver(pattern { f; f; f; f; b; f; f; f; f }.toMatrix(3, 3))

  case object Flare extends FireCard:
    val effectCode: Int = 1
    val effect: SpatialResolver = TokenResolver(pattern { f; f; f }.toMatrix(1, 3))

  case object BurningSnag extends FireCard:
    val effectCode: Int = 2
    val effect: SpatialResolver = TokenResolver(pattern { f; f; f; f }.toMatrix(2, 2))

  case object Ember extends FireCard:
    val effectCode: Int = 3
    val effect: SpatialResolver = TokenResolver(pattern(f).toMatrix(1, 1))

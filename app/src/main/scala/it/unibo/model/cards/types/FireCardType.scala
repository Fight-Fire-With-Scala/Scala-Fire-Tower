package it.unibo.model.cards.types

import it.unibo.model.cards.types.{b, f, pattern}
import it.unibo.model.cards.resolvers.{MultiStepResolver, Resolver}

sealed trait FireCardType extends HasEffect

// noinspection DuplicatedCode
case object FireCardType:
  val fireCards: Set[FireCardType] = Set(Explosion, Flare, BurningSnag, Ember)

  case object Explosion extends FireCardType:
    val effectCode: Int = 0
    val effect: Resolver = MultiStepResolver(pattern { f; f; f; f; b; f; f; f; f }.mapTo(3, 3))

  case object Flare extends FireCardType:
    val effectCode: Int = 1
    val effect: Resolver = MultiStepResolver(pattern { f; f; f }.mapTo(1, 3))

  case object BurningSnag extends FireCardType:
    val effectCode: Int = 2
    val effect: Resolver = MultiStepResolver(pattern { f; f; f; f }.mapTo(2, 2))

  case object Ember extends FireCardType:
    val effectCode: Int = 3
    val effect: Resolver = MultiStepResolver(pattern(f).mapTo(1, 1))

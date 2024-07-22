package it.unibo.model.cards.types

import it.unibo.model.cards.types.{f, pattern, w}
import it.unibo.model.cards.resolvers.{MultiStepResolver, Resolver}

sealed trait WaterCardType extends HasEffect

// noinspection DuplicatedCode
case object WaterCardType:
  val waterCards: Set[WaterCardType] = Set(SmokeJumper, AirDrop, FireEngine, Bucket)

  case object SmokeJumper extends WaterCardType:
    val effectCode: Int = 11
    val effect: Resolver = MultiStepResolver(pattern { w; w; w; w; f; w; w; w; w }.mapTo(3, 3))

  case object AirDrop extends WaterCardType:
    val effectCode: Int = 12
    val effect: Resolver = MultiStepResolver(pattern { w; w; w }.mapTo(1, 3))

  case object FireEngine extends WaterCardType:
    val effectCode: Int = 13
    val effect: Resolver = MultiStepResolver(pattern { w; w; w; w }.mapTo(2, 2))

  case object Bucket extends WaterCardType:
    val effectCode: Int = 14
    val effect: Resolver = MultiStepResolver(pattern { w; w; w }.mapTo(1, 3))

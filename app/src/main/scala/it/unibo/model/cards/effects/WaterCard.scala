package it.unibo.model.cards.effects

import it.unibo.model.cards.effects.patterns.{f, pattern, w}
import it.unibo.model.cards.resolvers.tokens.PatternResolver
import it.unibo.model.cards.resolvers.SpatialResolver
import it.unibo.model.cards.effects.HasSpatialEffect

trait WaterCard extends HasSpatialEffect

// noinspection DuplicatedCode
case object WaterCard:
  val waterCards: Set[WaterCard] = Set(SmokeJumper, AirDrop, FireEngine, Bucket)

  case object SmokeJumper extends WaterCard:
    val effectCode: Int = 11
    val effect: SpatialResolver =
      PatternResolver(pattern { w; w; w; w; f; w; w; w; w })

  case object AirDrop extends WaterCard:
    val effectCode: Int = 12
    val effect: SpatialResolver = PatternResolver(pattern { w; w; w })

  case object FireEngine extends WaterCard:
    val effectCode: Int = 13
    val effect: SpatialResolver = PatternResolver(pattern { w; w; w; w })

  case object Bucket extends WaterCard:
    val effectCode: Int = 14
    val effect: SpatialResolver = PatternResolver(pattern { w; w; w })

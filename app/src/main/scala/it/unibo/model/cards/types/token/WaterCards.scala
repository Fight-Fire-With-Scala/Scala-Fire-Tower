package it.unibo.model.cards.types.token

import it.unibo.model.cards._
import it.unibo.model.cards.resolver.{PatternResolver, Resolver}
import it.unibo.model.cards.types.HasSpatialEffect

trait WaterCards extends HasSpatialEffect[Resolver]

case object WaterCards:
  val waterCards: Set[WaterCards] = Set(SmokeJumper, AirDrop, FireEngine, Bucket)

  case object SmokeJumper extends WaterCards:
    val effectCode: Int = 11
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    //noinspection DuplicatedCode
    val spatialPattern: Matrix[PatternCell] = pattern { w; w; w; w; f; w; w; w; w }.toMatrix(3, 3)

  case object AirDrop extends WaterCards:
    val effectCode: Int = 12
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern { w; w; w }.toMatrix(1, 3)

  case object FireEngine extends WaterCards:
    val effectCode: Int = 13
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    //noinspection DuplicatedCode
    val spatialPattern: Matrix[PatternCell] = pattern { w; w; w; w }.toMatrix(2, 2)
  
  case object Bucket extends WaterCards:
    val effectCode: Int = 14
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern { w; w; w }.toMatrix(1, 3)
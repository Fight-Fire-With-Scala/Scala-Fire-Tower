package it.unibo.model.cards.types.token

import it.unibo.model.cards._
import it.unibo.model.cards.resolver.{PatternResolver, Resolver}
import it.unibo.model.cards.types.HasSpatialEffect

trait FireCards extends HasSpatialEffect[Resolver]

case object FireCards:
  val fireCards: Set[FireCards] = Set(Explosion, Flare, BurningSnag, Ember)

  case object Explosion extends FireCards:
    val effectCode: Int = 0
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    // noinspection DuplicatedCode
    val spatialPattern: Matrix[PatternCell] = pattern { f; f; f; f; b; f; f; f; f }.toMatrix(3, 3)

  case object Flare extends FireCards:
    val effectCode: Int = 1
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern { f; f; f }.toMatrix(1, 3)

  case object BurningSnag extends FireCards:
    val effectCode: Int = 5
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    // noinspection DuplicatedCode
    val spatialPattern: Matrix[PatternCell] = pattern { f; f; f; f }.toMatrix(2, 2)

  case object Ember extends FireCards:
    val effectCode: Int = 3
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern(f).toMatrix(1, 1)

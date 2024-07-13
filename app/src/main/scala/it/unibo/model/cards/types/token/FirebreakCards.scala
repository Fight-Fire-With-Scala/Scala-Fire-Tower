package it.unibo.model.cards.types.token

import it.unibo.model.cards._
import it.unibo.model.cards.resolver.{PatternResolver, Resolver}
import it.unibo.model.cards.types.HasSpatialEffect

trait FirebreakCards extends HasSpatialEffect[Resolver]

case object FirebreakCards:
  val firebreakCards: Set[FirebreakCards] = Set(DeReforest, ScratchLine, DozerLine)

  case object DeReforest extends FirebreakCards:
    val effectCode: Int = 10
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern(b).toMatrix(1, 1)

  case object ScratchLine extends FirebreakCards:
    val effectCode: Int = 9
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern { b; e; b }.toMatrix(1, 3)

  case object DozerLine extends FirebreakCards:
    val effectCode: Int = 8
    val effect: () => Resolver = () => PatternResolver(spatialPattern)
    val spatialPattern: Matrix[PatternCell] = pattern { b; b }.toMatrix(1, 2)

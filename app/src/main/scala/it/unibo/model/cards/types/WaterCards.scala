package it.unibo.model.cards.types

import it.unibo.model.cards.patterns.{f, pattern, w}
import it.unibo.model.cards.resolvers.tokens.{Token, TokenResolver}
import it.unibo.model.cards.resolvers.LinearResolver

trait WaterCards extends HasSingleEffect[Token]

// noinspection DuplicatedCode
case object WaterCards:
  val waterCards: Set[WaterCards] = Set(SmokeJumper, AirDrop, FireEngine, Bucket)

  case object SmokeJumper extends WaterCards:
    val effectCode: Int = 11
    val effect: LinearResolver[Token] =
      TokenResolver(pattern { w; w; w; w; f; w; w; w; w }.toMatrix(3, 3))

  case object AirDrop extends WaterCards:
    val effectCode: Int = 12
    val effect: LinearResolver[Token] = TokenResolver(pattern { w; w; w }.toMatrix(1, 3))

  case object FireEngine extends WaterCards:
    val effectCode: Int = 13
    val effect: LinearResolver[Token] = TokenResolver(pattern { w; w; w; w }.toMatrix(2, 2))

  case object Bucket extends WaterCards:
    val effectCode: Int = 14
    val effect: LinearResolver[Token] = TokenResolver(pattern { w; w; w }.toMatrix(1, 3))

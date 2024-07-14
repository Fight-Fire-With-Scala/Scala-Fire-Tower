package it.unibo.model.cards.types

import it.unibo.model.cards.patterns.{b, f, pattern}
import it.unibo.model.cards.resolvers.tokens.{Token, TokenResolver}
import it.unibo.model.cards.resolvers.LinearResolver

trait FireCards extends HasSingleEffect[Token]

// noinspection DuplicatedCode
case object FireCards:
  val fireCards: Set[FireCards] = Set(Explosion, Flare, BurningSnag, Ember)

  case object Explosion extends FireCards:
    val effectCode: Int = 0
    val effect: LinearResolver[Token] =
      TokenResolver(pattern { f; f; f; f; b; f; f; f; f }.toMatrix(3, 3))

  case object Flare extends FireCards:
    val effectCode: Int = 1
    val effect: LinearResolver[Token] = TokenResolver(pattern { f; f; f }.toMatrix(1, 3))

  case object BurningSnag extends FireCards:
    val effectCode: Int = 2
    val effect: LinearResolver[Token] = TokenResolver(pattern { f; f; f; f }.toMatrix(2, 2))
  
  case object Ember extends FireCards:
    val effectCode: Int = 3
    val effect: LinearResolver[Token] = TokenResolver(pattern(f).toMatrix(1, 1))

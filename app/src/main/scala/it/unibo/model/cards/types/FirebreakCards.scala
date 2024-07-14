package it.unibo.model.cards.types

import it.unibo.model.cards.patterns.{b, e, pattern}
import it.unibo.model.cards.resolvers.tokens.{Token, TokenResolver}
import it.unibo.model.cards.resolvers.LinearResolver

trait FirebreakCards extends HasSingleEffect[Token]

// noinspection DuplicatedCode
case object FirebreakCards:
  val firebreakCards: Set[FirebreakCards] = Set(DeReforest, ScratchLine, DozerLine)

  case object DeReforest extends FirebreakCards:
    val effectCode: Int = 10
    val effect: LinearResolver[Token] = TokenResolver(pattern(b).toMatrix(1, 1))

  case object ScratchLine extends FirebreakCards:
    val effectCode: Int = 9
    val effect: LinearResolver[Token] = TokenResolver(pattern { b; e; b }.toMatrix(1, 3))

  case object DozerLine extends FirebreakCards:
    val effectCode: Int = 8
    val effect: LinearResolver[Token] = TokenResolver(pattern { b; b }.toMatrix(1, 2))
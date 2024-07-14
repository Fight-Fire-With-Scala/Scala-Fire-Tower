package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.cards.patterns.SpatialPattern
import it.unibo.model.cards.resolvers.LinearResolver

case class TokenResolver(pattern: Option[SpatialPattern]) extends LinearResolver[Token]:
  override def resolve(): Token = ???
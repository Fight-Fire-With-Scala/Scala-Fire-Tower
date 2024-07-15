package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.SpatialPattern
import it.unibo.model.cards.PatternChoice
import it.unibo.model.cards.resolvers.SpatialResolver

case class TokenResolver(pattern: Option[SpatialPattern]) extends SpatialResolver:
  override def resolve(): PatternChoice =
    // convert from Map[Position, Token]
    // to List[Position] using Prolog/Scala
    // to List[Map[Position, Token]] to allow to display and choose the pattern to apply
    // to PatternChoice since the data
    ???

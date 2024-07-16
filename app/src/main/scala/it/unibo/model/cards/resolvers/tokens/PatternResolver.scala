package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.SpatialPattern
import it.unibo.model.cards.resolvers.{ResolverWithChoice, SpatialResolver}

case class PatternResolver(pattern: Option[SpatialPattern]) extends SpatialResolver:
  override def resolve(): PatternToApply =
    // convert from Map[Position, Token]
    // to List[Position] using Prolog/Scala
    // to List[Map[Position, Token]] to allow to display and choose the pattern to apply
    // to PatternChoice since the data
    ???

case class PatternResolverWithChoice(pattern: Option[SpatialPattern])
    extends ResolverWithChoice[PatternChoice, PatternToApply]:
  override def resolve(choice: PatternChoice): PatternToApply = ???

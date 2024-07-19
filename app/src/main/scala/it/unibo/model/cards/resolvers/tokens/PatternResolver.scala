package it.unibo.model.cards.resolvers.tokens

import it.unibo.model.cards.effects.Token
import it.unibo.model.cards.resolvers.{LinearResolver, ResolverWithChoice, SpatialResolver}
import it.unibo.model.grid.Position

case class SequentialPatternResolver() extends LinearResolver[PatternToApply]:
  override def resolve(): PatternToApply = ???

case class PatternResolver(pattern: Map[Position, Token]) extends SpatialResolver:
  override def resolve(): PatternToApply =
    // convert from Map[Position, Token]
    // to List[Map[Position, Token]] to allow to display and choose the pattern to apply
    // to PatternChoice since the data
    ???

// TODO: add list of patterns
case class PatternResolverWithChoice(pattern: Map[Position, Token])
    extends ResolverWithChoice[PatternChoice, PatternToApply]:
  override def resolve(choice: PatternChoice): PatternToApply = ???

package it.unibo.model.cards

import cats.syntax.either.*
import it.unibo.model.AppliedSpatialPattern
import it.unibo.model.cards.resolvers.Resolver
import it.unibo.model.cards.effects.{FireCard, FirebreakCard, WaterCard, WindCard}

trait GameEffect

trait GameChoice

case class PatternChoice(patterns: List[AppliedSpatialPattern]) extends GameEffect

val allCards = WaterCard.waterCards ++ FirebreakCard.firebreakCards ++ FireCard.fireCards ++
  WindCard.windCards

case class Card(title: String, description: String, resolve: Resolver) extends BaseCard

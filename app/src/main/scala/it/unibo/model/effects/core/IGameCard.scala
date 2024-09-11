package it.unibo.model.effects.core

trait IOffensiveCard

trait IDefensiveCard

trait CanBeDiscarded

trait CanBePlayedAsExtra

trait CannotBeDiscarded

trait IGameCard[CardEffect <: ICardEffect]:
  val id: Int
  val title: String
  val description: String
  val effect: CardEffect

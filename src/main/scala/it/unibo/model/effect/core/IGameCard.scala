package it.unibo.model.effect.core

trait ICanBeDiscarded

trait ICanBePlayedAsExtra

trait ICannotBeDiscarded

trait IGameCard[CardEffect <: ICardEffect]:
  val id: Int
  val title: String
  val description: String
  val effect: CardEffect

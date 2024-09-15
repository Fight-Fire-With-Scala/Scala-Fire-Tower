package it.unibo.model.effect.core

trait CanBeDiscarded

trait CanBePlayedAsExtra

trait CannotBeDiscarded

trait IGameCard[CardEffect <: ICardEffect]:
  val id: Int
  val title: String
  val description: String
  val effect: CardEffect

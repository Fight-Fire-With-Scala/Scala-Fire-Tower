package it.unibo.model.card

import it.unibo.model.effect.core.ICardEffect

trait ICanBeDiscarded

trait ICanBePlayedAsExtra

trait ICannotBeDiscarded

trait IGameCard:
  val id: Int
  val title: String
  val description: String
  val effect: ICardEffect

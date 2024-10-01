package it.unibo.model.card

import it.unibo.model.effect.core.CardEffect

trait ICanBeDiscarded

trait ICanBePlayedAsExtra

trait ICannotBeDiscarded

trait GameCard:
  val id: Int
  val title: String
  val description: String
  val effect: CardEffect

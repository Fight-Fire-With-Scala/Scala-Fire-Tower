package it.unibo.model.effect.core

sealed trait ICardEffect extends IGameEffect:
  val effectId: Int

trait IStandardCardEffect extends ICardEffect with CanBeDiscarded

trait ISpecialCardEffect extends ICardEffect with CanBePlayedAsExtra with CannotBeDiscarded
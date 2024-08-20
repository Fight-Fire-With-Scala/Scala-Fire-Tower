package it.unibo.model.cards.types

trait CannotBeDiscarded

trait CanBeDiscarded:
  var discarded: Boolean = false
  def toggleDiscarded(): Unit = discarded = !discarded

package it.unibo.model.cards.effects

sealed trait WindEffect extends CardEffect

object WindEffect:
  val windDirections: Set[WindEffect] = Set(North, South, East, West)

  case object North extends WindEffect
  case object South extends WindEffect
  case object East extends WindEffect
  case object West extends WindEffect
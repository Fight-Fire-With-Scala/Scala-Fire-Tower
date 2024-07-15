package it.unibo.model.cards.resolvers.wind

import it.unibo.model.cards.GameEffect

trait WindDirection extends GameEffect

object WindDirection:
  val windDirections: Set[WindDirection] = Set(North, South, East, West)

  case object North extends WindDirection
  case object South extends WindDirection
  case object East extends WindDirection
  case object West extends WindDirection
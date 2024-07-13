package it.unibo.model.cards.types.wind

trait WindDirection

object WindDirection:
  val windDirections: Seq[WindDirection] = Seq(North, South, East, West)

  case object North extends WindDirection
  case object South extends WindDirection
  case object East extends WindDirection
  case object West extends WindDirection
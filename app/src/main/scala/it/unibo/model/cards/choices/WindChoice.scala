package it.unibo.model.cards.choices

sealed trait WindChoice extends CardChoice

case object WindChoice:
  val windChoices: Set[WindChoice] = Set(UpdateWind, RandomUpdateWind, PlaceFire)

  case object UpdateWind extends WindChoice
  case object RandomUpdateWind extends WindChoice
  case object PlaceFire extends WindChoice
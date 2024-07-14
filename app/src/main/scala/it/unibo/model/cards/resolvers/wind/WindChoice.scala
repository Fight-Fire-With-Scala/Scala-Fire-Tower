package it.unibo.model.cards.resolvers.wind

import it.unibo.model.cards.resolvers.GameChoice

trait WindChoice extends GameChoice

case object WindChoice:
  val windChoices: Set[WindChoice] = Set(UpdateWind, RandomUpdateWind, PlaceFire)

  case object UpdateWind extends WindChoice
  case object RandomUpdateWind extends WindChoice
  case object PlaceFire extends WindChoice
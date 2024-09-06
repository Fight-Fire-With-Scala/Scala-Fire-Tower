package it.unibo.model.cards.choices

import it.unibo.model.cards.effects.PatternChoiceEffect

sealed trait GameChoice

enum StepChoice extends GameChoice:
  case PatternComputation
  case PatternApplication(p: PatternChoiceEffect)

sealed trait CardChoice extends GameChoice

enum WindChoice extends CardChoice:
  case UpdateWind
  case RandomUpdateWind
  case PlaceFire

package it.unibo.model.cards.choices

import it.unibo.model.cards.effects.PatternChoiceEffect

sealed trait GameChoice

enum StepChoice extends GameChoice derives CanEqual:
  case PatternComputation
  case PatternApplication(p: PatternChoiceEffect)

sealed trait CardChoice extends GameChoice

enum WindChoice extends CardChoice derives CanEqual:
  case UpdateWind
  case RandomUpdateWind
  case PlaceFire

enum FirebreakChoice extends CardChoice derives CanEqual:
  case Reforest
  case Deforest

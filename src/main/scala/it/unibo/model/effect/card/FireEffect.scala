package it.unibo.model.effect.card

import it.unibo.model.effect.core.{IStandardCardEffect, LogicEffectSolver, OffensiveEffect}
import it.unibo.model.gameboard.PatternType.{LargeEffect, MediumEffect, VeryLargeEffect, VerySmallEffect, given_Conversion_PatternType_Map}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak, Water}
import it.unibo.model.effect.core.SingleStepEffect.given_Conversion_ILogicComputation_SingleStepEffect
import it.unibo.model.effect.core.SingleStepEffect.given_Conversion_List_SingleStepEffect
import it.unibo.model.prolog.Rule

enum FireEffect(override val effectId: Int) extends IStandardCardEffect:
  case Explosion extends FireEffect(0)
  case Flare extends FireEffect(1)
  case BurningSnag extends FireEffect(2)
  case Ember extends FireEffect(3)

object FireEffect:
  val fireEffectSolver: LogicEffectSolver[FireEffect] = LogicEffectSolver:
    case Explosion =>
      OffensiveEffect(
        VeryLargeEffect(Map("a" -> Fire, "b" -> Firebreak)),
        Rule("explosion")
      )
    case Flare       => OffensiveEffect(MediumEffect(Map("a" -> Fire)), Rule("fire"))
    case BurningSnag => OffensiveEffect(LargeEffect(Map("a" -> Fire)), Rule("fire"))
    case Ember =>
      List(
        OffensiveEffect(
          VerySmallEffect(Map("a" -> Water)),
          Rule("ember_first_phase")
        ),
        OffensiveEffect(
          VerySmallEffect(Map("a" -> Fire)),
          Rule("ember_second_phase")
        )
      )

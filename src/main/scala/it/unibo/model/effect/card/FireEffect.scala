package it.unibo.model.effect.card

import it.unibo.model.effect.core.{IStandardCardEffect, LogicEffectSolver, OffensiveEffect}
import it.unibo.model.gameboard.PatternType.{LargeEffect, MediumEffect, VeryLargeEffect, VerySmallEffect, given_Conversion_PatternType_Map}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak, Water}
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_ILogicComputation_PatternLogicEffect
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_List_PatternLogicEffect
import it.unibo.model.prolog.Rule

// tag::fireEffect[]
enum FireEffect(override val effectId: Int) extends IStandardCardEffect:
  case Explosion extends FireEffect(1)
  case Flare extends FireEffect(2)
  case BurningSnag extends FireEffect(3)
  case Ember extends FireEffect(4)

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
// end::fireEffect[]
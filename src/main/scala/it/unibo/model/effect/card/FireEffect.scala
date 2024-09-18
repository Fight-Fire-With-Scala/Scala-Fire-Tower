package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ IStandardCardEffect, LogicEffectSolver, OffensiveEffect }
import it.unibo.model.gameboard.PatternType.{ given_Conversion_PatternType_Pattern, LargePattern, MediumPattern, VeryLargePattern, VerySmallPattern }
import it.unibo.model.gameboard.grid.ConcreteToken.{ Fire, Firebreak, Water }
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
        VeryLargePattern(Map("a" -> Fire, "b" -> Firebreak)),
        Rule("explosion")
      )
    case Flare       => OffensiveEffect(MediumPattern(Map("a" -> Fire)), Rule("fire"))
    case BurningSnag => OffensiveEffect(LargePattern(Map("a" -> Fire)), Rule("fire"))
    case Ember =>
      List(
        OffensiveEffect(
          VerySmallPattern(Map("a" -> Water)),
          Rule("ember_first_phase")
        ),
        OffensiveEffect(
          VerySmallPattern(Map("a" -> Fire)),
          Rule("ember_second_phase")
        )
      )
// end::fireEffect[]

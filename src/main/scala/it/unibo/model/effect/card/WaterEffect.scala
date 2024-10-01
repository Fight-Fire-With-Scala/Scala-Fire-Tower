package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ DefensiveEffect, StandardCardEffect, LogicEffectSolver }
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_LogicComputation_PatternLogicEffect
import it.unibo.model.gameboard.PatternType.LargePattern
import it.unibo.model.gameboard.PatternType.MediumPattern
import it.unibo.model.gameboard.PatternType.VeryLargePattern
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Pattern
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Water
import it.unibo.model.reasoner.Rule

enum WaterEffect(override val effectId: Int) extends StandardCardEffect:
  case SmokeJumper extends WaterEffect(12)
  case AirDrop extends WaterEffect(13)
  case FireEngine extends WaterEffect(14)

object WaterEffect:
  val waterEffectSolver: LogicEffectSolver[WaterEffect] = LogicEffectSolver:
    case SmokeJumper =>
      DefensiveEffect(
        VeryLargePattern(Map("a" -> Water, "b" -> Fire)),
        Rule("smoke_jumper")
      )
    case AirDrop => DefensiveEffect(MediumPattern(Map("a" -> Water)), Rule("water"))
    case FireEngine =>
      DefensiveEffect(LargePattern(Map("a" -> Water)), Rule("water"))

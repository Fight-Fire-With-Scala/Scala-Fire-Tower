package it.unibo.model.effect.card

import it.unibo.model.effect.core.IDefensiveCard
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_Function_List
import it.unibo.model.effect.core.IStandardCardEffect
import it.unibo.model.effect.core.LogicEffectSolver
import it.unibo.model.gameboard.PatternType.LargeEffect
import it.unibo.model.gameboard.PatternType.MediumEffect
import it.unibo.model.gameboard.PatternType.VeryLargeEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Water
import it.unibo.model.prolog.Rule

enum WaterEffect(override val effectId: Int) extends IStandardCardEffect with IDefensiveCard:
  case SmokeJumper extends WaterEffect(11)
  case AirDrop extends WaterEffect(12)
  case FireEngine extends WaterEffect(13)

object WaterEffect:
  val waterEffectSolver: LogicEffectSolver[WaterEffect] = LogicEffectSolver:
    case SmokeJumper =>
      ILogicEffect(VeryLargeEffect(Map("a" -> Water, "b" -> Fire)), Rule("smoke_jumper"))
    case AirDrop     => ILogicEffect(MediumEffect(Map("a" -> Water)), Rule("water"))
    case FireEngine  => ILogicEffect(LargeEffect(Map("a" -> Water)), Rule("water"))
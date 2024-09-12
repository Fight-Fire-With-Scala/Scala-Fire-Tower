package it.unibo.model.effects.cards

import it.unibo.model.effects.core.GameEffectResolver
import it.unibo.model.effects.core.IDefensiveCard
import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.effects.core.ILogicEffect
import it.unibo.model.effects.core.IStandardCardEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.LargeEffect
import it.unibo.model.gameboard.PatternType.MediumEffect
import it.unibo.model.gameboard.PatternType.VeryLargeEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Water
import it.unibo.model.prolog.Rule

enum WaterEffect(override val effectId: Int) extends IStandardCardEffect with IDefensiveCard:
  case SmokeJumper extends WaterEffect(11)
  case AirDrop extends WaterEffect(12)
  case FireEngine extends WaterEffect(13)

object WaterEffect:
  val waterEffectResolver: GameEffectResolver[IGameEffect, ILogicEffect] = GameEffectResolver:
    case WaterEffect.SmokeJumper => ILogicEffect(
        pattern = VeryLargeEffect(Map("a" -> Water, "b" -> Fire)).compilePattern,
        goals = List(Rule("smoke_jumper")),
        directions = Direction.values.toList
      )
    case WaterEffect.AirDrop     => ILogicEffect(
        pattern = MediumEffect(Map("a" -> Water)).compilePattern,
        goals = List(Rule("water")),
        directions = Direction.values.toList
      )
    case WaterEffect.FireEngine  => ILogicEffect(
        pattern = LargeEffect(Map("a" -> Water)).compilePattern,
        goals = List(Rule("water")),
        directions = Direction.values.toList
      )

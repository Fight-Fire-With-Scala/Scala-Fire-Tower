package it.unibo.model.effects.cards

import it.unibo.model.effects.core.{
  GameEffectResolver,
  IDefensiveCard,
  IGameEffect,
  ILogicEffect,
  IStandardCardEffect
}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Water}
import it.unibo.model.prolog.Rule
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.{LargeEffect, MediumEffect, VeryLargeEffect}

enum WaterEffect(override val effectId: Int) extends IStandardCardEffect with IDefensiveCard:
  case SmokeJumper extends WaterEffect(11)
  case AirDrop extends WaterEffect(12)
  case FireEngine extends WaterEffect(13)

object WaterEffect:
  val waterEffectResolver: GameEffectResolver[IGameEffect, ILogicEffect] = GameEffectResolver {
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
  }

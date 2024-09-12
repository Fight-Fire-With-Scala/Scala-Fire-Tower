package it.unibo.model.effect.card

import it.unibo.model.effect.core.GameEffectResolver
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.effect.core.IOffensiveCard
import it.unibo.model.effect.core.IStandardCardEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.LargeEffect
import it.unibo.model.gameboard.PatternType.MediumEffect
import it.unibo.model.gameboard.PatternType.VeryLargeEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Firebreak
import it.unibo.model.prolog.Rule

enum FireEffect(override val effectId: Int) extends IStandardCardEffect with IOffensiveCard:
  case Explosion extends FireEffect(0)
  case Flare extends FireEffect(1)
  case BurningSnag extends FireEffect(2)
  case Ember extends FireEffect(3)

object FireEffect:
  val fireEffectResolver: GameEffectResolver[IGameEffect, ILogicEffect] = GameEffectResolver:
    case FireEffect.Explosion   => ILogicEffect(
        pattern = VeryLargeEffect(Map("a" -> Fire, "b" -> Firebreak)).compilePattern,
        goals = List(Rule("explosion")),
        directions = Direction.values.toList
      )
    case FireEffect.Flare       => ILogicEffect(
        pattern = MediumEffect(Map("a" -> Fire)).compilePattern,
        goals = List(Rule("fire")),
        directions = Direction.values.toList
      )
    case FireEffect.BurningSnag => ILogicEffect(
        pattern = LargeEffect(Map("a" -> Fire)).compilePattern,
        goals = List(Rule("fire")),
        directions = Direction.values.toList
      )
    case FireEffect.Ember       => ???
    //    val patternEffect = PatternComputation(
    //      pattern = VerySmallEffect(Map("a" -> Firebreak)).compilePattern,
    //      goals = List(Rule("fire")),
    //      directions = Direction.values.toList
    //    )
    //    patternEffectResolver.resolve(patternEffect)

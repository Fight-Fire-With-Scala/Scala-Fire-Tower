package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ ILogicComputation, ILogicEffect, IOffensiveCard, IStandardCardEffect, LogicEffectSolver }
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_ILogicComputation_ILogicEffect
import it.unibo.model.gameboard.PatternType.LargeEffect
import it.unibo.model.gameboard.PatternType.MediumEffect
import it.unibo.model.gameboard.PatternType.VeryLargeEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.ConcreteToken.Firebreak
import it.unibo.model.prolog.Rule

enum FireEffect(override val effectId: Int) extends IStandardCardEffect with IOffensiveCard:
  case Explosion extends FireEffect(0)
  case Flare extends FireEffect(1)
  case BurningSnag extends FireEffect(2)
  case Ember extends FireEffect(3)

object FireEffect:
  val fireEffectSolver: LogicEffectSolver[FireEffect] = LogicEffectSolver:
    case Explosion =>
      ILogicComputation(VeryLargeEffect(Map("a" -> Fire, "b" -> Firebreak)), Rule("explosion"))
    case Flare       => ILogicComputation(MediumEffect(Map("a" -> Fire)), Rule("fire"))
    case BurningSnag => ILogicComputation(LargeEffect(Map("a" -> Fire)), Rule("fire"))
    case Ember       => ???
    //    val patternEffect = PatternComputation(
    //      pattern = VerySmallEffect(Map("a" -> Firebreak)).compilePattern,
    //      goals = List(Rule("fire")),
    //      directions = Direction.values.toList
    //    )
    //    patternEffectSolver.solve(patternEffect)

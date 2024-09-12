package it.unibo.model.effects.cards

import it.unibo.model.effects.core.{
  GameEffectResolver,
  IDefensiveCard,
  IGameEffect,
  ILogicEffect,
  IStandardCardEffect
}
import it.unibo.model.gameboard.PatternType.{MediumAltEffect, SmallEffect, VerySmallEffect}
import it.unibo.model.prolog.Rule
import it.unibo.model.gameboard.grid.ConcreteToken.{Empty, Firebreak}
import it.unibo.model.gameboard.Direction

enum FirebreakEffect(override val effectId: Int) extends IStandardCardEffect with IDefensiveCard:
  case DozerLine extends FirebreakEffect(8)
  case ScratchLine extends FirebreakEffect(9)
  case DeReforest extends FirebreakEffect(10)

object FirebreakEffect:
  val fireBreakEffectResolver: GameEffectResolver[IGameEffect, ILogicEffect] = GameEffectResolver:
    case FirebreakEffect.DeReforest  => ILogicEffect(
        pattern = VerySmallEffect(Map("a" -> Firebreak)).compilePattern,
        goals = List(Rule("deforest"), Rule("reforest")),
        directions = Direction.values.toList
      )
    case FirebreakEffect.ScratchLine => ILogicEffect(
        pattern = MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)).compilePattern,
        goals = List(Rule("scratch_line")),
        directions = Direction.values.toList
      )
    case FirebreakEffect.DozerLine   => ILogicEffect(
        pattern = SmallEffect(Map("a" -> Firebreak)).compilePattern,
        goals = List(Rule("dozer_line")),
        directions = Direction.values.toList
      )

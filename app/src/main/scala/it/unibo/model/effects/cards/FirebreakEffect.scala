package it.unibo.model.effects.cards

import it.unibo.model.effects.core.GameEffectResolver
import it.unibo.model.effects.core.IDefensiveCard
import it.unibo.model.effects.core.IGameEffect
import it.unibo.model.effects.core.ILogicEffect
import it.unibo.model.effects.core.IStandardCardEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.PatternType.MediumAltEffect
import it.unibo.model.gameboard.PatternType.SmallEffect
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Empty
import it.unibo.model.gameboard.grid.ConcreteToken.Firebreak
import it.unibo.model.prolog.Rule

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

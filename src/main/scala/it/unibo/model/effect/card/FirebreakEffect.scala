package it.unibo.model.effect.card

import it.unibo.model.effect.core.IDefensiveCard
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_Function_List
import it.unibo.model.effect.core.IStandardCardEffect
import it.unibo.model.effect.core.LogicEffectSolver
import it.unibo.model.gameboard.PatternType.MediumAltEffect
import it.unibo.model.gameboard.PatternType.SmallEffect
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.Empty
import it.unibo.model.gameboard.grid.ConcreteToken.Firebreak
import it.unibo.model.prolog.Rule

enum FirebreakEffect(override val effectId: Int) extends IStandardCardEffect with IDefensiveCard:
  case DozerLine extends FirebreakEffect(8)
  case ScratchLine extends FirebreakEffect(9)
  case DeReforest extends FirebreakEffect(10)

object FirebreakEffect:
  val fireBreakEffectSolver: LogicEffectSolver[FirebreakEffect] = LogicEffectSolver:
    case DeReforest  =>
      ILogicEffect(VerySmallEffect(Map("a" -> Firebreak)), List(Rule("deforest"), Rule("reforest")))
    case ScratchLine =>
      ILogicEffect(MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)), Rule("scratch_line"))
    case DozerLine   => ILogicEffect(SmallEffect(Map("a" -> Firebreak)), Rule("dozer_line"))

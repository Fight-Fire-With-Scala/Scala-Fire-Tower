package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ DefensiveEffect, IStandardCardEffect, LogicEffectSolver, OffensiveEffect }
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_List_PatternLogicEffect
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_ILogicComputation_PatternLogicEffect
import it.unibo.model.gameboard.PatternType.MediumAltEffect
import it.unibo.model.gameboard.PatternType.SmallEffect
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.{ Empty, Firebreak, Reforest }
import it.unibo.model.prolog.Rule

enum FirebreakEffect(override val effectId: Int) extends IStandardCardEffect:
  case DozerLine extends FirebreakEffect(9)
  case ScratchLine extends FirebreakEffect(10)
  case DeReforest extends FirebreakEffect(11)

object FirebreakEffect:
  val fireBreakEffectSolver: LogicEffectSolver[FirebreakEffect] = LogicEffectSolver:
    case DeReforest =>
      List(
        DefensiveEffect(VerySmallEffect(Map("a" -> Firebreak)), Rule("deforest")),
        OffensiveEffect(VerySmallEffect(Map("a" -> Reforest)), Rule("reforest"))
      )
    case ScratchLine =>
      DefensiveEffect(
        MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)),
        Rule("scratch_line")
      )
    case DozerLine =>
      DefensiveEffect(SmallEffect(Map("a" -> Firebreak)), Rule("dozer_line"))

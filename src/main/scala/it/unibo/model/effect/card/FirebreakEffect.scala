package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ DefensiveEffect, StandardCardEffect, LogicEffectSolver, OffensiveEffect }
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_List_PatternLogicEffect
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_LogicComputation_PatternLogicEffect
import it.unibo.model.gameboard.PatternType.MediumAltPattern
import it.unibo.model.gameboard.PatternType.SmallPattern
import it.unibo.model.gameboard.PatternType.VerySmallPattern
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Pattern
import it.unibo.model.gameboard.grid.ConcreteToken.{ Empty, Firebreak, Reforest }
import it.unibo.model.reasoner.Rule

enum FirebreakEffect(override val effectId: Int) extends StandardCardEffect:
  case DozerLine extends FirebreakEffect(9)
  case ScratchLine extends FirebreakEffect(10)
  case DeReforest extends FirebreakEffect(11)

object FirebreakEffect:
  val fireBreakEffectSolver: LogicEffectSolver[FirebreakEffect] = LogicEffectSolver:
    case DeReforest =>
      List(
        DefensiveEffect(VerySmallPattern(Map("a" -> Firebreak)), Rule("deforest")),
        OffensiveEffect(VerySmallPattern(Map("a" -> Reforest)), Rule("reforest"))
      )
    case ScratchLine =>
      DefensiveEffect(
        MediumAltPattern(Map("a" -> Firebreak, "b" -> Empty)),
        Rule("scratch_line")
      )
    case DozerLine =>
      DefensiveEffect(SmallPattern(Map("a" -> Firebreak)), Rule("dozer_line"))

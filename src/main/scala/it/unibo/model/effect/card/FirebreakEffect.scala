package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ IDefensiveCard, ILogicComputation, ILogicEffect, IStandardCardEffect, LogicEffectSolver }
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_ILogicComputation_ILogicEffect
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_List_ILogicEffect
import it.unibo.model.gameboard.PatternType.MediumAltEffect
import it.unibo.model.gameboard.PatternType.SmallEffect
import it.unibo.model.gameboard.PatternType.VerySmallEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.{ Empty, Firebreak, Reforest }
import it.unibo.model.prolog.Rule

enum FirebreakEffect(override val effectId: Int) extends IStandardCardEffect with IDefensiveCard:
  case DozerLine extends FirebreakEffect(8)
  case ScratchLine extends FirebreakEffect(9)
  case DeReforest extends FirebreakEffect(10)

object FirebreakEffect:
  val fireBreakEffectSolver: LogicEffectSolver[FirebreakEffect] = LogicEffectSolver:
    case DeReforest =>
      List(
        ILogicComputation(VerySmallEffect(Map("a" -> Firebreak)), Rule("deforest")),
        ILogicComputation(VerySmallEffect(Map("a" -> Reforest)), Rule("reforest"))
      )
    case ScratchLine =>
      ILogicComputation(MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)), Rule("scratch_line"))
    case DozerLine => ILogicComputation(SmallEffect(Map("a" -> Firebreak)), Rule("dozer_line"))

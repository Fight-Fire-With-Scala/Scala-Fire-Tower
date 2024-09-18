package it.unibo.model.effect.core

import it.unibo.model.prolog.Rule
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_ILogicComputation_List

sealed trait ILogicEffect extends IGameEffect:
  val computations: List[ILogicComputation]

object ILogicEffect:
  given Conversion[ILogicComputation, List[ILogicComputation]] = List(_)

  def apply(): ILogicEffect =
    PatternLogicEffect(OffensiveEffect(Map.empty, Rule(""), List.empty))

final case class PatternLogicEffect(computations: List[ILogicComputation]) extends ILogicEffect

object PatternLogicEffect:
  given Conversion[ILogicComputation, PatternLogicEffect]       = PatternLogicEffect(_)
  given Conversion[List[ILogicComputation], PatternLogicEffect] = PatternLogicEffect(_)

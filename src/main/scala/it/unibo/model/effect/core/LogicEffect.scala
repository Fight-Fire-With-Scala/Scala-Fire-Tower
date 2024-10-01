package it.unibo.model.effect.core

import it.unibo.model.reasoner.Rule
import it.unibo.model.effect.core.LogicEffect.given_Conversion_LogicComputation_List

sealed trait LogicEffect extends GameEffect:
  val computations: List[LogicComputation]

object LogicEffect:
  given Conversion[LogicComputation, List[LogicComputation]] = List(_)

  def apply(): LogicEffect =
    PatternLogicEffect(OffensiveEffect(Map.empty, Rule(""), List.empty))

final case class PatternLogicEffect(computations: List[LogicComputation]) extends LogicEffect

object PatternLogicEffect:
  given Conversion[LogicComputation, PatternLogicEffect]       = PatternLogicEffect(_)
  given Conversion[List[LogicComputation], PatternLogicEffect] = PatternLogicEffect(_)

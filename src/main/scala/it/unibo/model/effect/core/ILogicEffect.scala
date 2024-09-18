package it.unibo.model.effect.core

import it.unibo.model.prolog.Rule
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_ILogicComputation_List

sealed trait ILogicEffect extends IGameEffect:
  val computations: List[ILogicComputation]

object ILogicEffect:
  given Conversion[ILogicComputation, List[ILogicComputation]] = List(_)

  def apply(): ILogicEffect =
    SingleStepEffect(OffensiveEffect(Map.empty, Rule(""), List.empty))

final case class SingleStepEffect(computations: List[ILogicComputation]) extends ILogicEffect

object SingleStepEffect:
  given Conversion[ILogicComputation, SingleStepEffect]       = SingleStepEffect(_)
  given Conversion[List[ILogicComputation], SingleStepEffect] = SingleStepEffect(_)

final case class MultiStepEffect(computations: List[ILogicComputation]) extends ILogicEffect

object MultiStepEffect:
  given Conversion[ILogicComputation, MultiStepEffect]       = MultiStepEffect(_)
  given Conversion[List[ILogicComputation], MultiStepEffect] = MultiStepEffect(_)

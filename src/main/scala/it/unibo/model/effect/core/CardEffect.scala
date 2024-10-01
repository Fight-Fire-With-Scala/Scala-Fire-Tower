package it.unibo.model.effect.core

import it.unibo.model.card.{ ICanBeDiscarded, ICanBePlayedAsExtra, ICannotBeDiscarded }
import it.unibo.model.effect.card.{ BucketEffect, FireEffect, FirebreakEffect, WaterEffect, WindEffect }
import it.unibo.model.effect.core.PatternLogicEffect.given_Conversion_LogicComputation_PatternLogicEffect

trait StandardCardEffect extends CardEffect with ICanBeDiscarded

trait SpecialCardEffect extends CardEffect with ICanBePlayedAsExtra with ICannotBeDiscarded

sealed trait CardEffect extends GameEffect:
  val effectId: Int

object CardEffect:
  // tag::contextualLogicEffect[]
  given Conversion[CardEffect, LogicEffect] = convert(_)

  def convert(effect: CardEffect): LogicEffect = effect match
    case e: StandardCardEffect =>
      e match
        case e: FireEffect      => FireEffect.fireEffectSolver.solve(e)
        case e: FirebreakEffect => FirebreakEffect.fireBreakEffectSolver.solve(e)
        case e: WaterEffect     => WaterEffect.waterEffectSolver.solve(e)
        case e: WindEffect      => WindEffect.windEffectSolver.solve(e)
        case _                  => LogicEffect()
    case e: SpecialCardEffect =>
      e match
        case BucketEffect => BucketEffect.bucketEffect
        case _            => LogicEffect()
  // end::contextualLogicEffect[]

  given Conversion[List[CardEffect], List[LogicEffect]] = _.map(e => convert(e))

  given Conversion[CardEffect, String] =
    case effect: StandardCardEffect =>
      effect match
        case effect: FireEffect      => "fire"
        case effect: FirebreakEffect => "firebreak"
        case effect: WaterEffect     => "water"
        case effect: WindEffect      => "wind"
        case _                       => "not-found"
    case effect: SpecialCardEffect =>
      effect match
        case BucketEffect => "water"
        case _            => "not-found"

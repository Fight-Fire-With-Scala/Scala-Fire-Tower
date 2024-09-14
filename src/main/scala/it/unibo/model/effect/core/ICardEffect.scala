package it.unibo.model.effect.core

import it.unibo.model.effect.card.{ BucketEffect, FireEffect, FirebreakEffect, WaterEffect, WindEffect }

sealed trait ICardEffect extends IGameEffect:
  val effectId: Int

trait IStandardCardEffect extends ICardEffect with CanBeDiscarded

trait ISpecialCardEffect extends ICardEffect with CanBePlayedAsExtra with CannotBeDiscarded

given Conversion[ICardEffect, ILogicEffect] = {
  case e: IStandardCardEffect =>
    e match
      case e: FireEffect      => FireEffect.fireEffectSolver.solve(e)
      case e: FirebreakEffect => FirebreakEffect.fireBreakEffectSolver.solve(e)
      case e: WaterEffect     => WaterEffect.waterEffectSolver.solve(e)
      case e: WindEffect      => WindEffect.windEffectSolver.solve(e)
      case _                  => ILogicEffect()
  case e: ISpecialCardEffect =>
    e match
      case BucketEffect => BucketEffect.bucketEffect
      case _            => ILogicEffect()
}

given Conversion[ICardEffect, String] = {
  case effect: IStandardCardEffect =>
    effect match
      case effect: FireEffect      => "fire"
      case effect: FirebreakEffect => "firebreak"
      case effect: WaterEffect     => "water"
      case effect: WindEffect      => "wind"
      case _                       => "not-found"
  case effect: ISpecialCardEffect =>
    effect match
      case BucketEffect => "water"
      case _            => "not-found"
}

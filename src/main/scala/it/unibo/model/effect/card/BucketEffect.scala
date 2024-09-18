package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ ILogicComputation, ISpecialCardEffect, OffensiveEffect }
import it.unibo.model.gameboard.PatternType.MediumEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.Water
import it.unibo.model.prolog.Rule

case object BucketEffect extends ISpecialCardEffect:
  override val effectId: Int = 15

  val bucketEffect: ILogicComputation =
    OffensiveEffect(MediumEffect(Map("a" -> Water)), Rule("bucket"))

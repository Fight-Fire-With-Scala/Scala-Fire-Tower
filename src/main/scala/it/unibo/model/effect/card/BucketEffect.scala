package it.unibo.model.effect.card

import it.unibo.model.effect.core.{ LogicComputation, SpecialCardEffect, OffensiveEffect }
import it.unibo.model.gameboard.PatternType.MediumPattern
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Pattern
import it.unibo.model.gameboard.grid.ConcreteToken.Water
import it.unibo.model.reasoner.Rule

case object BucketEffect extends SpecialCardEffect:
  override val effectId: Int = 15

  val bucketEffect: LogicComputation =
    OffensiveEffect(MediumPattern(Map("a" -> Water)), Rule("bucket"))

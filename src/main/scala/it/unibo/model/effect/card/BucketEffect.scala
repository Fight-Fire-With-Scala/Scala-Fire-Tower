package it.unibo.model.effect.card

import it.unibo.model.effect.core.IDefensiveCard
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.effect.core.ILogicEffect.given_Conversion_Function_List
import it.unibo.model.effect.core.ISpecialCardEffect
import it.unibo.model.gameboard.PatternType.MediumEffect
import it.unibo.model.gameboard.PatternType.given_Conversion_PatternType_Map
import it.unibo.model.gameboard.grid.ConcreteToken.Water
import it.unibo.model.prolog.Rule

case object BucketEffect extends ISpecialCardEffect with IDefensiveCard:
  override val effectId: Int = 14

  val bucketEffect: ILogicEffect = ILogicEffect(MediumEffect(Map("a" -> Water)), Rule("bucket"))
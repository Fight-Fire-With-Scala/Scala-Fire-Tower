package it.unibo.model.effects.cards

import it.unibo.model.effects.core.{
  GameEffectResolver,
  IGameEffect,
  ILogicEffect,
  IOffensiveCard,
  IStandardCardEffect
}
import it.unibo.model.gameboard.Direction

enum WindEffect(override val effectId: Int) extends IStandardCardEffect with IOffensiveCard:
  case North extends WindEffect(4)
  case South extends WindEffect(5)
  case East extends WindEffect(6)
  case West extends WindEffect(7)

object WindEffect:
  val windEffectResolver: GameEffectResolver[IGameEffect, ILogicEffect] = GameEffectResolver:
    case WindEffect.North => WindChoiceEffect.getPlaceFireEffect(Direction.North)
    case WindEffect.East  => WindChoiceEffect.getPlaceFireEffect(Direction.East)
    case WindEffect.West  => WindChoiceEffect.getPlaceFireEffect(Direction.West)
    case WindEffect.South => WindChoiceEffect.getPlaceFireEffect(Direction.South)

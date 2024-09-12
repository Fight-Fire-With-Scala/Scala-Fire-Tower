package it.unibo.model.effect.card

import it.unibo.model.effect.core.GameEffectResolver
import it.unibo.model.effect.core.IGameEffect
import it.unibo.model.effect.core.ILogicEffect
import it.unibo.model.effect.core.IOffensiveCard
import it.unibo.model.effect.core.IStandardCardEffect
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

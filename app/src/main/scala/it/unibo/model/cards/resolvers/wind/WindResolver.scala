package it.unibo.model.cards.resolvers.wind

import it.unibo.model.cards.effects.{f, pattern}
import it.unibo.model.cards.resolvers.{Dice, ResolverWithChoice}
import it.unibo.model.cards.resolvers.tokens.PatternResolver
import it.unibo.model.cards.resolvers.wind.WindChoice.{PlaceFire, RandomUpdateWind, UpdateWind}
import it.unibo.model.cards.GameEffect

case class WindResolver(direction: WindDirection, dice: Dice[WindDirection])
    extends ResolverWithChoice[WindChoice, GameEffect]:
  
  def updateCurrentWind(newWindDirection: WindDirection): WindResolver =
    copy(direction = newWindDirection)

  override def resolve(choice: WindChoice): GameEffect = choice match
    case UpdateWind       => direction
    case RandomUpdateWind => dice.roll()
    case PlaceFire        => PatternResolver(pattern(f).toMap(1, 1)).resolve()
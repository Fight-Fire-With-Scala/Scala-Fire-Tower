package it.unibo.model.cards.resolvers.wind

import it.unibo.model.cards.effects.patterns.{f, pattern}
import it.unibo.model.cards.resolvers.{Dice, ResolverWithChoice}
import it.unibo.model.cards.resolvers.tokens.TokenResolver
import it.unibo.model.cards.resolvers.wind.WindChoice.{PlaceFire, RandomUpdateWind, UpdateWind}
import it.unibo.model.cards.GameEffect

case class WindResolver(direction: WindDirection, dice: Dice[WindDirection])
    extends ResolverWithChoice[WindChoice, GameEffect]:
  
  def updateCurrentWind(newWindDirection: WindDirection): WindResolver =
    copy(direction = newWindDirection)

  override def resolve(choice: WindChoice): GameEffect = choice match
    case UpdateWind       => direction
    case RandomUpdateWind => dice.roll()
    case PlaceFire        => TokenResolver(pattern(f).toMatrix(1, 1)).resolve()
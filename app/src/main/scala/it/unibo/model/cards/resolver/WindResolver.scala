package it.unibo.model.cards.resolver

import it.unibo.model.cards.types.wind.WindDirection

case class WindResolver(currentWind: WindDirection, seed: Long = System.currentTimeMillis)
    extends Resolver:
  private val windDice: Dice[WindDirection] = Dice(WindDirection.windDirections, seed)
  def rollForWindDirection(): WindResolver = copy(currentWind = windDice.roll())
  def updateWindDirection(newWindDirection: WindDirection): WindResolver =
    copy(currentWind = newWindDirection)
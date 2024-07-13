package it.unibo.model.cards

import it.unibo.model.cards.resolver.{Dice, WindResolver}
import it.unibo.model.cards.types.wind.WindDirection
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WindResolverSpec extends AnyWordSpecLike with Matchers:

  "A WindResolver" should:
    "initialize with a given wind direction" in:
      val windManager = WindResolver(WindDirection.North)
      windManager.currentWind shouldBe WindDirection.North

    "roll for a new wind direction" in:
      val fixedSeed = 42L
      val windManager = WindResolver(WindDirection.North, fixedSeed)
      val newWindManager = windManager.rollForWindDirection()

      newWindManager.currentWind should not be windManager.currentWind
      WindDirection.windDirections should contain(newWindManager.currentWind)

    "update the wind direction" in:
      val windManager = WindResolver(WindDirection.North)
      val updatedWindManager = windManager.updateWindDirection(WindDirection.South)

      updatedWindManager.currentWind shouldBe WindDirection.South

  "A Dice" should:
    "return a value from the provided values" in:
      val values = Seq(1, 2, 3, 4)
      val dice = Dice(values, 42L)
      values should contain (dice.roll())

    "return deterministic results with a fixed seed" in:
      val values = Seq(WindDirection.North, WindDirection.South, WindDirection.East, WindDirection.West)
      val dice1 = Dice(values, 42L)
      val dice2 = Dice(values, 42L)

      dice1.roll() shouldBe dice2.roll()
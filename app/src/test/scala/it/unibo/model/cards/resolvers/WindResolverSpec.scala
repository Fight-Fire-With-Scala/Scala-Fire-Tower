package it.unibo.model.cards.resolvers

import it.unibo.model.cards.resolvers.Dice
import it.unibo.model.cards.resolvers.wind.WindChoice.{RandomUpdateWind, UpdateWind}
import it.unibo.model.cards.resolvers.wind.{WindDirection, WindResolver}
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WindResolverSpec extends AnyWordSpecLike with Matchers:

  "A Dice" should:
    "return a value from the provided values" in:
      val values = Seq(1, 2, 3, 4)
      val dice = Dice(values, 42L)
      values should contain(dice.roll())

    "return deterministic results with a fixed seed" in:
      val values =
        Seq(WindDirection.North, WindDirection.South, WindDirection.East, WindDirection.West)
      val dice1 = Dice(values, 42L)
      val dice2 = Dice(values, 42L)

      dice1.roll() shouldBe dice2.roll()

  "A WindResolver" should:
    val dice = Dice(WindDirection.windDirections.toSeq, 42L)
    val windResolver = WindResolver(WindDirection.North, dice)

    "initialize with a given wind direction" in:
      windResolver.updateCurrentWind(WindDirection.South).direction shouldBe WindDirection.South

    "roll for a new wind direction" in:
      windResolver.resolve(RandomUpdateWind) shouldBe WindDirection.East

    "update the wind direction" in:
      windResolver.resolve(UpdateWind) shouldBe WindDirection.North

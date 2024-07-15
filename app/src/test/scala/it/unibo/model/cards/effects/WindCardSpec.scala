package it.unibo.model.cards.effects

import it.unibo.model.cards.effects.WindCard
import it.unibo.model.cards.resolvers.wind.WindResolver
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WindCardSpec extends AnyWordSpecLike with Matchers:

  "WindCards" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code for each wind card" in:
      WindCard.North.effectCode shouldBe 4
      WindCard.South.effectCode shouldBe 5
      WindCard.East.effectCode shouldBe 6
      WindCard.West.effectCode shouldBe 7

    "generate the correct resolver" in:
      WindCard.North.effect shouldBe a[WindResolver]
      WindCard.South.effect shouldBe a[WindResolver]
      WindCard.East.effect shouldBe a[WindResolver]
      WindCard.West.effect shouldBe a[WindResolver]

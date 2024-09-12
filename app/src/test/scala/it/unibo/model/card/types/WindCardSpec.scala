package it.unibo.model.card.types

import it.unibo.model.effect.core.WindResolver
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WindCardSpec extends AnyWordSpecLike with Matchers:

  "WindCards" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code for each wind card" in:
      WindCard.North.id shouldBe 4
      WindCard.South.id shouldBe 5
      WindCard.East.id shouldBe 6
      WindCard.West.id shouldBe 7

    "generate the correct resolver" in:
      WindCard.North.effect shouldBe a[WindResolver]
      WindCard.South.effect shouldBe a[WindResolver]
      WindCard.East.effect shouldBe a[WindResolver]
      WindCard.West.effect shouldBe a[WindResolver]

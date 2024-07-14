package it.unibo.model.cards.effects

import it.unibo.model.cards.types.WindCards
import it.unibo.model.cards.resolvers.wind.WindResolver
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WindCardsSpec extends AnyWordSpecLike with Matchers:

  "WindCards" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code for each wind card" in:
      WindCards.North.effectCode shouldBe 4
      WindCards.South.effectCode shouldBe 5
      WindCards.East.effectCode shouldBe 6
      WindCards.West.effectCode shouldBe 7

    "generate the correct resolver" in:
      WindCards.North.effect shouldBe a[WindResolver]
      WindCards.South.effect shouldBe a[WindResolver]
      WindCards.East.effect shouldBe a[WindResolver]
      WindCards.West.effect shouldBe a[WindResolver]

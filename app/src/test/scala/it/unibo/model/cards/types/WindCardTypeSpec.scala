package it.unibo.model.cards.types

import it.unibo.model.cards.choices.WindChoice
import it.unibo.model.cards.resolvers.ChoiceResolver
import it.unibo.model.cards.types.WindCardType
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WindCardTypeSpec extends AnyWordSpecLike with Matchers:

  "WindCards" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code for each wind card" in:
      WindCardType.North.effectCode shouldBe 4
      WindCardType.South.effectCode shouldBe 5
      WindCardType.East.effectCode shouldBe 6
      WindCardType.West.effectCode shouldBe 7

    "generate the correct resolver" in:
      WindCardType.North.effect shouldBe a[ChoiceResolver[WindChoice]]
      WindCardType.South.effect shouldBe a[ChoiceResolver[WindChoice]]
      WindCardType.East.effect shouldBe a[ChoiceResolver[WindChoice]]
      WindCardType.West.effect shouldBe a[ChoiceResolver[WindChoice]]

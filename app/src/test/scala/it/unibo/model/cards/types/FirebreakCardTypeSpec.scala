package it.unibo.model.cards.types

import it.unibo.model.cards.choices.FirebreakChoice
import it.unibo.model.cards.resolvers.{ChoiceResolver, MultiStepResolver}
import it.unibo.model.cards.types.FirebreakCardType.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FirebreakCardTypeSpec extends GameEffectAbstractSpec:

  "Cards of type Firebreak" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      DeReforest.effectCode shouldBe 10
      ScratchLine.effectCode shouldBe 9
      DozerLine.effectCode shouldBe 8

    "generate the correct resolver" in:
      DeReforest.effect shouldBe a[ChoiceResolver[FirebreakChoice]]
      ScratchLine.effect shouldBe a[MultiStepResolver]
      DozerLine.effect shouldBe a[MultiStepResolver]

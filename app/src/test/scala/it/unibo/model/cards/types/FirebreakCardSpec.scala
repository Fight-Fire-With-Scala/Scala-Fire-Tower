package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.FirebreakResolver
import it.unibo.model.cards.types.FirebreakCard.*
import it.unibo.model.effects.core.MultiStepResolver
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FirebreakCardSpec extends GameEffectAbstractSpec:

  "Cards of type Firebreak" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      DeReforest.id shouldBe 10
      ScratchLine.id shouldBe 9
      DozerLine.id shouldBe 8

    "generate the correct resolver" in:
      DeReforest.effect shouldBe a[FirebreakResolver]
      ScratchLine.effect shouldBe a[MultiStepResolver]
      DozerLine.effect shouldBe a[MultiStepResolver]

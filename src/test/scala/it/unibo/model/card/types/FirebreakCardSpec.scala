package it.unibo.model.card.types

import it.unibo.model.effect.card.FirebreakEffect
import it.unibo.model.effect.card.FirebreakEffect.DeReforest
import it.unibo.model.effect.core.MultiStepResolver

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

package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.{FirebreakResolver, MultiStepResolver}
import it.unibo.model.cards.types.FirebreakCard.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FirebreakCardSpec extends GameEffectAbstractSpec:

  "Cards of type Firebreak" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      DeReforest.effectCode shouldBe 10
      ScratchLine.effectCode shouldBe 9
      DozerLine.effectCode shouldBe 8

    "generate the correct resolver" in:
      DeReforest.effect shouldBe a[FirebreakResolver]
      ScratchLine.effect shouldBe a[MultiStepResolver]
      DozerLine.effect shouldBe a[MultiStepResolver]
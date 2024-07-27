package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.MultiStepResolver
import it.unibo.model.cards.types.FireCard.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FireCardTypeSpec extends GameEffectAbstractSpec:

  "Cards of type Fire" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      Explosion.effectCode shouldBe 0
      Flare.effectCode shouldBe 1
      BurningSnag.effectCode shouldBe 2
      Ember.effectCode shouldBe 3

    "generate the correct resolver" in:
      Explosion.effect shouldBe a[MultiStepResolver]
      Flare.effect shouldBe a[MultiStepResolver]
      BurningSnag.effect shouldBe a[MultiStepResolver]
      Ember.effect shouldBe a[MultiStepResolver]

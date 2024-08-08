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
      Explosion.id shouldBe 0
      Flare.id shouldBe 1
      BurningSnag.id shouldBe 2
      Ember.id shouldBe 3

    "generate the correct resolver" in:
      Explosion.effect shouldBe a[MultiStepResolver]
      Flare.effect shouldBe a[MultiStepResolver]
      BurningSnag.effect shouldBe a[MultiStepResolver]
      Ember.effect shouldBe a[MultiStepResolver]

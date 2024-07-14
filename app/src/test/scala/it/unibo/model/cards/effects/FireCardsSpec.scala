package it.unibo.model.cards.effects

import it.unibo.model.cards.resolvers.tokens.TokenResolver
import it.unibo.model.cards.types.FireCards.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FireCardsSpec extends GameEffectAbstractSpec:

  "Cards of type Fire" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      Explosion.effectCode shouldBe 0
      Flare.effectCode shouldBe 1
      BurningSnag.effectCode shouldBe 2
      Ember.effectCode shouldBe 3

    "generate the correct resolver" in:
      Explosion.effect shouldBe a[TokenResolver]
      Flare.effect shouldBe a[TokenResolver]
      BurningSnag.effect shouldBe a[TokenResolver]
      Ember.effect shouldBe a[TokenResolver]
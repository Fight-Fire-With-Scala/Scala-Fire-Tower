package it.unibo.model.cards.effects

import it.unibo.model.cards.resolvers.tokens.PatternResolver
import it.unibo.model.cards.effects.FireCard.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FireCardSpec extends GameEffectAbstractSpec:

  "Cards of type Fire" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      Explosion.effectCode shouldBe 0
      Flare.effectCode shouldBe 1
      BurningSnag.effectCode shouldBe 2
      Ember.effectCode shouldBe 3

    "generate the correct resolver" in:
      Explosion.effect shouldBe a[PatternResolver]
      Flare.effect shouldBe a[PatternResolver]
      BurningSnag.effect shouldBe a[PatternResolver]
      Ember.effect shouldBe a[PatternResolver]
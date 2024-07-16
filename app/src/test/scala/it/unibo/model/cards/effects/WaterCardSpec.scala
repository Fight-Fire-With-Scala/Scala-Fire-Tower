package it.unibo.model.cards.effects

import it.unibo.model.cards.resolvers.tokens.PatternResolver
import it.unibo.model.cards.resolvers.wind.WindResolver
import it.unibo.model.cards.effects.WaterCard.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WaterCardSpec extends GameEffectAbstractSpec:

  "Cards of type Water" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      SmokeJumper.effectCode shouldBe 11
      AirDrop.effectCode shouldBe 12
      FireEngine.effectCode shouldBe 13
      Bucket.effectCode shouldBe 14

    "generate the correct resolver" in:
      SmokeJumper.effect shouldBe a[PatternResolver]
      AirDrop.effect shouldBe a[PatternResolver]
      FireEngine.effect shouldBe a[PatternResolver]
      Bucket.effect shouldBe a[PatternResolver]

package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.MultiStepResolver
import it.unibo.model.cards.types.WaterCardType.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WaterCardTypeSpec extends GameEffectAbstractSpec:

  "Cards of type Water" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      SmokeJumper.effectCode shouldBe 11
      AirDrop.effectCode shouldBe 12
      FireEngine.effectCode shouldBe 13
      Bucket.effectCode shouldBe 14

    "generate the correct resolver" in:
      SmokeJumper.effect shouldBe a[MultiStepResolver]
      AirDrop.effect shouldBe a[MultiStepResolver]
      FireEngine.effect shouldBe a[MultiStepResolver]
      Bucket.effect shouldBe a[MultiStepResolver]

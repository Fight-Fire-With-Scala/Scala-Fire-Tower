package it.unibo.model.card.types

import it.unibo.model.card.types.WaterCard.*
import it.unibo.model.effect.core.MultiStepResolver
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WaterCardSpec extends GameEffectAbstractSpec:

  "Cards of type Water" should:

    // noinspection ScalaUnusedExpression
    "have the correct effect code" in:
      SmokeJumper.id shouldBe 11
      AirDrop.id shouldBe 12
      FireEngine.id shouldBe 13
      Bucket.id shouldBe 14

    "generate the correct resolver" in:
      SmokeJumper.effect shouldBe a[MultiStepResolver]
      AirDrop.effect shouldBe a[MultiStepResolver]
      FireEngine.effect shouldBe a[MultiStepResolver]
      Bucket.effect shouldBe a[MultiStepResolver]

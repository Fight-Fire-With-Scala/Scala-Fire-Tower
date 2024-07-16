package it.unibo.model.cards.effects

import it.unibo.model.cards.resolvers.tokens.{PatternResolver, PatternResolverWithChoice}
import it.unibo.model.cards.effects.FirebreakCard.*
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
      DeReforest.effect shouldBe a[PatternResolverWithChoice]
      ScratchLine.effect shouldBe a[PatternResolver]
      DozerLine.effect shouldBe a[PatternResolver]

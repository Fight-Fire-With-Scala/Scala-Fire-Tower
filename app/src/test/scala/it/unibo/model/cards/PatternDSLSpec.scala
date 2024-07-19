package it.unibo.model.cards

import it.unibo.model.cards.effects.{b, e, f, pattern, w, Empty, Fire, Firebreak, Water}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class PatternDSLSpec extends AnyWordSpecLike with Matchers:

  "Pattern DSL" should:
    val dummyPattern = pattern { w; f; b; e }

    "accumulate cells correctly" in:
      dummyPattern.tokens shouldBe ArrayBuffer(Water(), Fire(), Firebreak(), Empty())

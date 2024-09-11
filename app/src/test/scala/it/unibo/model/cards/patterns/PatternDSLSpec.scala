package it.unibo.model.cards.patterns

import it.unibo.model.cards.patterns.*
import it.unibo.model.effects.{e, f, k, pattern, w}
import it.unibo.model.gameboard.grid.ConcreteToken.*
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class PatternDSLSpec extends AnyWordSpecLike with Matchers:

  "Pattern DSL" should:
    val dummyPattern = pattern { w; f; k; e }

    "accumulate cells correctly" in:
      dummyPattern.tokens.toSeq shouldEqual ArrayBuffer(Water, Fire, Firebreak, Empty).toSeq

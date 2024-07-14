package it.unibo.model.cards

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import it.unibo.model.cards.patterns.{b, e, f, pattern, w}
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class PatternDSLSpec extends AnyWordSpecLike with Matchers:

  "Pattern DSL" should:
    val dummyPattern = pattern { w; f; b; e }
    val dummyMatrix = dummyPattern.toMatrix(2, 2).get

    "accumulate cells correctly" in:
      logger.info(s"${dummyPattern.cells}")
      dummyPattern.cells shouldBe ArrayBuffer(Water(), Fire(), Firebreak(), Empty())

    // noinspection ScalaUnusedExpression
    "convert to matrix correctly" in:
      dummyMatrix.rows shouldBe 2
      dummyMatrix.cols shouldBe 2
      dummyMatrix(0, 0) shouldBe a [Water]
      dummyMatrix(0, 1) shouldBe a [Fire]
      dummyMatrix(1, 0) shouldBe a [Firebreak]
      dummyMatrix(1, 1) shouldBe a [Empty]

    "throw an exception for incorrect matrix dimensions" in:
      dummyPattern.toMatrix(4, 2) shouldBe None

    "pretty print matrix correctly" in:
      dummyMatrix.prettyPrint()
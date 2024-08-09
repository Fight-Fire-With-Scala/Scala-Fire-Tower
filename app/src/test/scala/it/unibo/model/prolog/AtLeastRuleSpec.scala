package it.unibo.model.prolog

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AtLeastRuleSpec extends AbstractPrologSpec:

  "The at least rule" should:
    "return true if at least a cell in the pattern has a neighbor of the given type" in:
      val goal = AtLeast("neigh", "cell", dummyPattern, dummyTrueRequiredCells)
      engine.isSolvedWithSuccess(goal) shouldBe true

    "return true if at least a cell in the pattern is of the given type" in:
      val goal = AtLeast("pattern", "cell", dummyPattern, dummyTrueRequiredCells)
      engine.isSolvedWithSuccess(goal) shouldBe true

    "return true if at least a token in the pattern has a neighbor of the given type" in:
      val goal = AtLeast("neigh", "token", dummyPattern, dummyTrueRequiredTokens)
      engine.isSolvedWithSuccess(goal) shouldBe true

    "return true if at least a token in the pattern is of the given type" in:
      val goal = AtLeast("pattern", "token", dummyPattern, dummyTrueRequiredTokens)
      engine.isSolvedWithSuccess(goal) shouldBe true

    "return false if there aren't any cells in the pattern that have a neighbor of the given type" in :
      val goal = AtLeast("neigh", "cell", dummyPattern, dummyFalseRequiredCells)
      engine.isSolvedWithSuccess(goal) shouldBe false

    "return false if there aren't any cells in the pattern that are of the given type" in :
      val goal = AtLeast("pattern", "cell", dummyPattern, dummyFalseRequiredCells)
      engine.isSolvedWithSuccess(goal) shouldBe false

    "return false if there aren't any tokens in the pattern that have a neighbor of the given type" in:
      val goal = AtLeast("neigh", "token", dummyPattern, dummyFalseRequiredTokens)
      engine.isSolvedWithSuccess(goal) shouldBe false

    "return false if there aren't any tokens in the pattern that are of the given type" in:
      val goal = AtLeast("pattern", "token", dummyPattern, dummyFalseRequiredTokens)
      engine.isSolvedWithSuccess(goal) shouldBe false

package it.unibo.model.prolog

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AllRuleSpec extends AbstractPrologSpec:

  "The all rule" should :
    "return true if all the cells in the pattern are of the given type" in :
      val goal = All("cell", dummyPattern, dummyTrueRequiredCells)
      engine.isSolvedWithSuccess(goal) shouldBe true

    "return true if all the tokens in the pattern are of the given type" in:
      val dummyPattern = "[(0, 0, f), (0, 1, f)]"
      val goal = All("token", dummyPattern, dummyTrueRequiredTokens)
      engine.isSolvedWithSuccess(goal) shouldBe true

    "return false if at least a cell in the pattern is not of the given type" in :
      val goal = All("cell", dummyPattern, dummyFalseRequiredCells)
      engine.isSolvedWithSuccess(goal) shouldBe false

    "return false if at least a token in the pattern is not of the given type" in :
      val goal = All("token", dummyPattern, dummyFalseRequiredTokens)
      engine.isSolvedWithSuccess(goal) shouldBe false

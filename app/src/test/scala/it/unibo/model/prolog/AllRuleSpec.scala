package it.unibo.model.prolog

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import Scala2P.given

@RunWith(classOf[JUnitRunner])
class AllRuleSpec extends AbstractRuleSpec:

  "The all rule" should :
    "return true if all the cells in the pattern are of the given type" in :
      val trueCellGoal = All("cell", dummyPattern, dummyTrueRequiredCells)
      checkSolverResult(trueCellGoal) shouldBe true

//    "return true if all the tokens in the pattern are of the given type" in :
//      val trueTokenGoal = All("token", dummyPattern, dummyTrueRequiredCells)
//      solver(trueTokenGoal) shouldBe true

    "return false if at least a cell in the pattern is not of the given type" in :
      val falseCellGoal = All("cell", dummyPattern, dummyFalseRequiredCells)
      checkSolverResult(falseCellGoal) shouldBe false

    "return false if at least a token in the pattern is not of the given type" in :
      val falseTokenGoal = All("token", dummyPattern, dummyFalseRequiredCells)
      checkSolverResult(falseTokenGoal) shouldBe false

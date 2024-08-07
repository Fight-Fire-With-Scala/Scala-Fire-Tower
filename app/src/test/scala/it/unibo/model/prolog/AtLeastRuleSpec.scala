package it.unibo.model.prolog

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import Scala2P.given

@RunWith(classOf[JUnitRunner])
class AtLeastRuleSpec extends AbstractRuleSpec:

  "The at least rule" should:
    val trueCellGoal = AtLeast("neigh", "cell", dummyPattern, dummyTrueRequiredCells)
    val trueTokenGoal = AtLeast("neigh", "token", dummyPattern, dummyTrueRequiredTokens)
    val falseCellGoal = AtLeast("neigh", "cell", dummyPattern, dummyFalseRequiredCells)
    val falseTokenGoal = AtLeast("neigh", "token", dummyPattern, dummyFalseRequiredTokens)

    "return true if at least a cell in the pattern has a neighbor of the given type" in:
      checkSolverResult(trueCellGoal) shouldBe true

    "return true if at least a cell in the pattern is of the given type" in:
      checkSolverResult(trueCellGoal.copy(kind = "pattern")) shouldBe true

    "return true if at least a token in the pattern has a neighbor of the given type" in:
      checkSolverResult(trueTokenGoal) shouldBe true

    "return true if at least a token in the pattern is of the given type" in:
      checkSolverResult(trueTokenGoal.copy(kind = "pattern")) shouldBe true

    "return false if there aren't any cells in the pattern that have a neighbor of the given type" in :
      checkSolverResult(falseCellGoal) shouldBe false

    "return false if there aren't any cells in the pattern that are of the given type" in :
      checkSolverResult(falseCellGoal.copy(kind = "pattern")) shouldBe false

    "return false if there aren't any tokens in the pattern that have a neighbor of the given type" in:
      checkSolverResult(falseTokenGoal) shouldBe false

    "return false if there aren't any tokens in the pattern that are of the given type" in:
      checkSolverResult(falseTokenGoal.copy(kind = "pattern")) shouldBe false

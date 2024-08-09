package it.unibo.model.prolog

import it.unibo.model.gameboard.Direction.{North, South}
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.PrologUtils.parseComputedPatterns
import it.unibo.model.gameboard.grid.ConcreteToken.*

@RunWith(classOf[JUnitRunner])
class PatternFitRuleSpec extends AbstractPrologSpec:

  "The pattern fit rule" should:
    val goal = PatternFit(dummyPosition)

    "return the pattern applied in each cardinal direction" in:
      val sol = engine.solve(goal).map(i => parseComputedPatterns(i))
      sol.toList shouldBe List(
        Map(Position(0, 2) -> Fire, Position(-1, 2) -> Fire),
        Map(Position(0, 2) -> Fire, Position(1, 2) -> Fire),
        Map(Position(0, 2) -> Fire, Position(0, 3) -> Fire),
        Map(Position(0, 2) -> Fire, Position(0, 1) -> Fire)
      )

    "return the pattern applied only in one cardinal direction" in:
      val customDirections = List(North).map(_.getId)
      val customTheory = theory(customDirections, deltas)
      val engineMod = engine.copy(theory = customTheory)
      val sol = engineMod.solve(goal).map(i => parseComputedPatterns(i))

      sol.toList shouldBe List(
        Map(Position(0, 2) -> Fire, Position(-1, 2) -> Fire),
      )


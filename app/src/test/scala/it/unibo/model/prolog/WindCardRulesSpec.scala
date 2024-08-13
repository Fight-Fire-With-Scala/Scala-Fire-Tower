package it.unibo.model.prolog

import it.unibo.model.cards.effects.VerySmallEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class WindCardRulesSpec extends AbstractPrologSpec:

  // noinspection ScalaUnusedExpression
  "A wind card" should:
    "provide the correct choices to place a fire token in a given cardinal direction" in:
      val goal = Rule("fire")
      val pattern = VerySmallEffect(Map("a" -> Fire)).compilePattern
      val engine = buildEngine(pattern, Direction.North)

      val solNorth = engine.solveAsPatternList(goal)
      solNorth shouldEqual List(Map(Position(0, 1) -> Fire))

      val engineSouth = engine.copy(buildTheory(pattern, Direction.South))
      val solSouth = engineSouth.solveAsPatternList(goal)
      solSouth shouldEqual List(Map(Position(2, 1) -> Fire))

      val engineEast = engine.copy(buildTheory(pattern, Direction.East))
      val solEast = engineEast.solveAsPatternList(goal)
      solEast shouldEqual List(Map(Position(1, 2) -> Fire))

      val engineWest = engine.copy(buildTheory(pattern, Direction.West))
      val solWest = engineWest.solveAsPatternList(goal)
      solWest shouldEqual List(Map(Position(1, 0) -> Fire))
package it.unibo.model.prolog

import it.unibo.model.effects.VerySmallEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class WindCardRulesSpec extends AbstractPrologSpec:
  "A wind card" should:
    val goal = Rule("fire")
    val pattern = VerySmallEffect(Map("a" -> Fire)).compilePattern
    val engine = buildEngine(pattern, directions = Direction.North)

    "provide the correct choices to place a fire token in the north direction" in:
      val solNorth = engine.solveAsPatterns(goal)
      solNorth shouldEqual Set(Map(Position(1, 2) -> Fire))

    "provide the correct choices to place a fire token in the south direction" in:
      val engineSouth = engine.copy(buildTheory(pattern, directions = Direction.South))
      val solSouth = engineSouth.solveAsPatterns(goal)
      solSouth shouldEqual Set(Map(Position(3, 2) -> Fire))

    "provide the correct choices to place a fire token in the east direction" in:
      val engineEast = engine.copy(buildTheory(pattern, directions = Direction.East))
      val solEast = engineEast.solveAsPatterns(goal)
      solEast shouldEqual Set(Map(Position(2, 3) -> Fire))

    "provide the correct choices to place a fire token in the west direction" in:
      val engineWest = engine.copy(buildTheory(pattern, directions = Direction.West))
      val solWest = engineWest.solveAsPatterns(goal)
      solWest shouldEqual Set(Map(Position(2, 1) -> Fire))
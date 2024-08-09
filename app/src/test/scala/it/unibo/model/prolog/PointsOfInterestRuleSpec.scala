package it.unibo.model.prolog

import it.unibo.model.gameboard.grid.Position
import it.unibo.model.prolog.PrologUtils.parseComputedApplicationPoints
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PointsOfInterestRuleSpec extends AbstractPrologSpec:

  "The points of interest rule" should:
    val goal = PointsOfInterest(dummyAllowedCells, dummyAllowedTokens)

    "return the correct available application points" in:
      val sol = engine.solve(goal).map(i => parseComputedApplicationPoints(i))
      sol.toList shouldEqual LazyList(
        Position(0, 0),
        Position(0, 4),
        Position(4, 0),
        Position(4, 4),
        Position(0, 0),
        Position(0, 1)
      )

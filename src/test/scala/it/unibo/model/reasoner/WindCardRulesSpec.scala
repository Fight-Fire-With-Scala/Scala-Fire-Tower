package it.unibo.model.reasoner

import it.unibo.model.effect.card.WindEffect
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.reasoner.AbstractCardSolverSpec.given_Conversion_ICardEffect_ILogicComputation
import it.unibo.model.gameboard.grid.Position

class WindCardRulesSpec extends AbstractCardSolverSpec:
  "A wind card" should:

    "provide the correct choices to place a fire token in the north direction" in:
      val sol = getAvailablePatterns(WindEffect.North)
      sol shouldEqual Set(Map(Position(1, 2) -> Fire))

    "provide the correct choices to place a fire token in the south direction" in:
      val sol = getAvailablePatterns(WindEffect.South)
      sol shouldEqual Set(Map(Position(3, 2) -> Fire))

    "provide the correct choices to place a fire token in the east direction" in:
      val sol = getAvailablePatterns(WindEffect.East)
      sol shouldEqual Set(Map(Position(2, 3) -> Fire))

    "provide the correct choices to place a fire token in the west direction" in:
      val sol = getAvailablePatterns(WindEffect.West)
      sol shouldEqual Set(Map(Position(2, 1) -> Fire))

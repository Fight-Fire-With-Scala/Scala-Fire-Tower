package it.unibo.model.reasoner

import it.unibo.model.effect.card.BucketEffect
import it.unibo.model.gameboard.Pattern
import it.unibo.model.gameboard.grid.ConcreteToken.{ Fire, Water }
import it.unibo.model.reasoner.AbstractCardSolverSpec.given_Conversion_CardEffect_LogicComputation
import it.unibo.model.gameboard.grid.{ Position, Token }

class SpecialCardRulesSpec extends AbstractCardSolverSpec:

  override val defaultTokens: Pattern = Map(Position(0, 0) -> Fire)

  "A special card" should:
    "provide the correct choices to solve the bucket card" in:
      val sol = getAvailablePatterns(BucketEffect)
      sol shouldEqual Set(
        Map(Position(0, 0) -> Water),
        Map(Position(0, 0) -> Water, Position(1, 0) -> Water, Position(2, 0) -> Water),
        Map(Position(0, 0) -> Water, Position(0, 1) -> Water, Position(0, 2) -> Water),
        Map(Position(0, 1) -> Water, Position(0, 0) -> Water),
        Map(Position(0, 2) -> Water, Position(0, 1) -> Water, Position(0, 0) -> Water),
        Map(Position(1, 0) -> Water, Position(0, 0) -> Water),
        Map(Position(2, 0) -> Water, Position(1, 0) -> Water, Position(0, 0) -> Water)
      )

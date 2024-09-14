package it.unibo.model.prolog

import it.unibo.model.effect.card.BucketEffect
import it.unibo.model.effect.core.given_Conversion_ICardEffect_ILogicEffect
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Water}
import it.unibo.model.gameboard.grid.{Position, Token}

class SpecialCardRulesSpec extends AbstractCardSolverSpec:

  override val defaultTokens: Map[Position, Token] = Map(Position(1, 2) -> Fire)

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

package it.unibo.model.reasoner

import it.unibo.model.effect.card.WaterEffect
import it.unibo.model.gameboard.Pattern
import it.unibo.model.gameboard.grid.ConcreteToken.{ Fire, Water }
import it.unibo.model.reasoner.AbstractCardSolverSpec.given_Conversion_CardEffect_LogicComputation
import it.unibo.model.gameboard.grid.{ Position, Token }

class WaterCardRulesSpec extends AbstractCardSolverSpec:

  override val defaultTokens: Pattern = Map(Position(1, 2) -> Fire)

  "A water card" should:

    "provide the correct choices to solve the smoke jumper card" in:
      val sol = getAvailablePatterns(WaterEffect.SmokeJumper)
      sol shouldEqual Set(
        Map(
          Position(2, 2) -> Water,
          Position(2, 1) -> Water,
          Position(2, 3) -> Water,
          Position(0, 3) -> Water,
          Position(0, 2) -> Water,
          Position(1, 2) -> Fire,
          Position(1, 1) -> Water,
          Position(1, 3) -> Water,
          Position(0, 1) -> Water
        )
      )

    "provide the correct choices to solve the fire engine card" in:
      val sol = getAvailablePatterns(WaterEffect.FireEngine)
      sol shouldEqual Set(
        Map(
          Position(1, 2) -> Water,
          Position(0, 2) -> Water,
          Position(1, 3) -> Water,
          Position(0, 3) -> Water
        ),
        Map(
          Position(1, 2) -> Water,
          Position(2, 2) -> Water,
          Position(1, 1) -> Water,
          Position(2, 1) -> Water
        ),
        Map(
          Position(1, 2) -> Water,
          Position(1, 3) -> Water,
          Position(2, 2) -> Water,
          Position(2, 3) -> Water
        ),
        Map(
          Position(1, 2) -> Water,
          Position(1, 1) -> Water,
          Position(0, 2) -> Water,
          Position(0, 1) -> Water
        )
      )

    "provide the correct choices to solve the air drop card" in:
      val sol = getAvailablePatterns(WaterEffect.AirDrop)
      sol shouldEqual Set(
        Map(Position(1, 2) -> Water, Position(1, 1) -> Water, Position(1, 0) -> Water),
        Map(Position(0, 2) -> Water, Position(1, 2) -> Water, Position(2, 2) -> Water),
        Map(Position(1, 2) -> Water, Position(1, 3) -> Water, Position(1, 4) -> Water),
        Map(Position(1, 1) -> Water, Position(1, 2) -> Water, Position(1, 3) -> Water),
        Map(Position(1, 2) -> Water, Position(0, 2) -> Water),
        Map(Position(1, 2) -> Water, Position(2, 2) -> Water, Position(3, 2) -> Water)
      )

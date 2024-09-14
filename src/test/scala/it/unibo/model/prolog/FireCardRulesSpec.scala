package it.unibo.model.prolog

import it.unibo.model.effect.card.FireEffect
import it.unibo.model.gameboard.grid.ConcreteToken.{ Fire, Firebreak }
import it.unibo.model.gameboard.grid.{ Position, Token }
import it.unibo.model.effect.core.given_Conversion_ICardEffect_ILogicEffect
import it.unibo.model.prolog.PrologUtils.given_Conversion_Rule_Term

class FireCardRulesSpec extends AbstractCardSolverSpec:

  override val defaultTokens: Map[Position, Token] = Map(Position(1, 2) -> Fire)

  "A fire card" should:
    "provide the correct choices to solve the explosion card" in:
      val sol = getAvailablePatterns(FireEffect.Explosion)
      sol shouldEqual Set(
        Map(
          Position(2, 2) -> Fire,
          Position(2, 1) -> Fire,
          Position(2, 3) -> Fire,
          Position(0, 3) -> Fire,
          Position(0, 2) -> Fire,
          Position(1, 2) -> Firebreak,
          Position(1, 1) -> Fire,
          Position(1, 3) -> Fire,
          Position(0, 1) -> Fire
        )
      )

    "provide the correct choices to solve the burning snag card" in:
      val sol = getAvailablePatterns(FireEffect.BurningSnag)
      sol shouldEqual Set(
        Map(Position(2, 3) -> Fire, Position(2, 2) -> Fire, Position(1, 3) -> Fire),
        Map(
          Position(2, 1) -> Fire,
          Position(2, 2) -> Fire,
          Position(3, 1) -> Fire,
          Position(3, 2) -> Fire
        ),
        Map(Position(0, 1) -> Fire, Position(0, 2) -> Fire, Position(1, 1) -> Fire),
        Map(
          Position(2, 1) -> Fire,
          Position(3, 1) -> Fire,
          Position(2, 0) -> Fire,
          Position(3, 0) -> Fire
        ),
        Map(
          Position(1, 4) -> Fire,
          Position(1, 3) -> Fire,
          Position(0, 4) -> Fire,
          Position(0, 3) -> Fire
        ),
        Map(
          Position(2, 2) -> Fire,
          Position(2, 3) -> Fire,
          Position(3, 2) -> Fire,
          Position(3, 3) -> Fire
        ),
        Map(
          Position(2, 4) -> Fire,
          Position(3, 4) -> Fire,
          Position(2, 3) -> Fire,
          Position(3, 3) -> Fire
        ),
        Map(Position(0, 2) -> Fire, Position(0, 3) -> Fire),
        Map(Position(0, 3) -> Fire, Position(1, 3) -> Fire, Position(0, 2) -> Fire),
        Map(
          Position(4, 2) -> Fire,
          Position(3, 2) -> Fire,
          Position(4, 3) -> Fire,
          Position(3, 3) -> Fire
        ),
        Map(
          Position(3, 2) -> Fire,
          Position(4, 2) -> Fire,
          Position(3, 1) -> Fire,
          Position(4, 1) -> Fire
        ),
        Map(
          Position(1, 4) -> Fire,
          Position(2, 4) -> Fire,
          Position(1, 3) -> Fire,
          Position(2, 3) -> Fire
        ),
        Map(Position(0, 1) -> Fire, Position(0, 2) -> Fire),
        Map(
          Position(2, 1) -> Fire,
          Position(2, 0) -> Fire,
          Position(1, 1) -> Fire,
          Position(1, 0) -> Fire
        ),
        Map(Position(2, 1) -> Fire, Position(1, 1) -> Fire, Position(2, 2) -> Fire),
        Map(
          Position(0, 1) -> Fire,
          Position(1, 1) -> Fire,
          Position(0, 0) -> Fire,
          Position(1, 0) -> Fire
        )
      )

    "provide the correct choices to solve the flare up card" in:
      val sol = getAvailablePatterns(FireEffect.Flare)
      sol shouldEqual Set(
        Map(Position(2, 4) -> Fire, Position(2, 3) -> Fire),
        Map(Position(1, 0) -> Fire, Position(1, 1) -> Fire),
        Map(Position(3, 2) -> Fire, Position(2, 2) -> Fire),
        Map(Position(2, 1) -> Fire, Position(3, 1) -> Fire, Position(4, 1) -> Fire),
        Map(Position(4, 2) -> Fire, Position(3, 2) -> Fire),
        Map(Position(2, 2) -> Fire, Position(0, 2) -> Fire),
        Map(Position(0, 2) -> Fire),
        Map(Position(3, 4) -> Fire, Position(3, 3) -> Fire, Position(3, 2) -> Fire),
        Map(Position(2, 2) -> Fire, Position(3, 2) -> Fire, Position(4, 2) -> Fire),
        Map(Position(2, 2) -> Fire, Position(2, 3) -> Fire, Position(2, 4) -> Fire),
        Map(Position(0, 3) -> Fire, Position(1, 3) -> Fire, Position(2, 3) -> Fire),
        Map(Position(0, 4) -> Fire, Position(0, 3) -> Fire, Position(0, 2) -> Fire),
        Map(Position(0, 1) -> Fire, Position(1, 1) -> Fire, Position(2, 1) -> Fire),
        Map(Position(3, 1) -> Fire, Position(3, 2) -> Fire, Position(3, 3) -> Fire),
        Map(Position(3, 1) -> Fire, Position(2, 1) -> Fire, Position(1, 1) -> Fire),
        Map(Position(2, 2) -> Fire, Position(2, 1) -> Fire, Position(2, 0) -> Fire),
        Map(Position(0, 1) -> Fire, Position(0, 2) -> Fire, Position(0, 3) -> Fire),
        Map(Position(4, 3) -> Fire, Position(3, 3) -> Fire, Position(2, 3) -> Fire),
        Map(Position(0, 3) -> Fire, Position(1, 3) -> Fire),
        Map(Position(2, 1) -> Fire, Position(2, 2) -> Fire, Position(2, 3) -> Fire),
        Map(Position(0, 0) -> Fire, Position(0, 1) -> Fire, Position(0, 2) -> Fire),
        Map(Position(1, 3) -> Fire, Position(2, 3) -> Fire, Position(3, 3) -> Fire),
        Map(Position(2, 0) -> Fire, Position(2, 1) -> Fire),
        Map(Position(3, 2) -> Fire, Position(3, 1) -> Fire, Position(3, 0) -> Fire),
        Map(Position(0, 1) -> Fire, Position(1, 1) -> Fire),
        Map(Position(1, 4) -> Fire, Position(1, 3) -> Fire),
        Map(Position(1, 1) -> Fire, Position(1, 3) -> Fire)
      )

//    "provide the correct choices to solve the first phase of the ember card" in:
//      val fireEffect = FireEffect.Ember
//      val engine = buildEngine(Map(dummyCardId -> List(fireEffect)))
//      val sol = engine.solveAsPatterns(fireEffect.goals.head(dummyCardId))
//
//      sol shouldEqual Set(Map(Position(2, 3) -> Empty))
//
//    "provide the correct choices to solve the second phase of the ember card" in:
//      val fireEffect = FireEffect.Ember
//      val engine = buildEngine(Map(dummyCardId -> List(fireEffect)))
//      val sol = engine.solveAsPatterns(fireEffect.goals.head(dummyCardId))
//
//      sol shouldEqual Set(
//        Map(Position(1, 2) -> Fire),
//        Map(Position(2, 1) -> Fire),
//        Map(Position(2, 3) -> Fire),
//        Map(Position(3, 2) -> Fire)
//      )

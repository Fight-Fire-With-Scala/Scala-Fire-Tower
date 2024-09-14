package it.unibo.model.prolog

import it.unibo.model.effect.{LargeEffect, MediumEffect, VeryLargeEffect, VerySmallEffect}
import it.unibo.model.gameboard.grid.ConcreteToken.{Empty, Fire, Firebreak}
import it.unibo.model.gameboard.grid.Position

class FireCardRulesSpec extends AbstractPrologSpec:
  "A fire card" should:
    "provide the correct choices to resolve the explosion card" in:
      val goal = Rule("explosion")
      val pattern = VeryLargeEffect(Map("a" -> Fire, "b" -> Firebreak)).compilePattern
      val engine = buildEngine(pattern, tokens = Map(Position(1, 2) -> Fire))
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(Map(
        Position(2, 2) -> Fire,
        Position(2, 1) -> Fire,
        Position(2, 3) -> Fire,
        Position(0, 3) -> Fire,
        Position(0, 2) -> Fire,
        Position(1, 2) -> Firebreak,
        Position(1, 1) -> Fire, // Do not place here since it is ef
        Position(1, 3) -> Fire,
        Position(0, 1) -> Fire
      ))

    val goal = Rule("fire")

    "provide the correct choices to resolve the burning snag card" in:
      val pattern = LargeEffect(Map("a" -> Fire)).compilePattern
      val engine = buildEngine(pattern)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(
          Position(3, 2) -> Fire,
          Position(3, 3) -> Fire,
          Position(4, 2) -> Fire,
          Position(4, 3) -> Fire
        ),
        Map(
          Position(0, 1) -> Fire,
          Position(0, 2) -> Fire,
          Position(1, 1) -> Fire,
          Position(1, 2) -> Fire
        ),
        Map(
          Position(0, 2) -> Fire,
          Position(0, 3) -> Fire,
          Position(1, 2) -> Fire,
          Position(1, 3) -> Fire
        ),
        Map(
          Position(1, 3) -> Fire,
          Position(1, 4) -> Fire,
          Position(2, 3) -> Fire,
          Position(2, 4) -> Fire
        ),
        Map(
          Position(2, 0) -> Fire,
          Position(2, 1) -> Fire,
          Position(3, 0) -> Fire,
          Position(3, 1) -> Fire
        ),
        Map(
          Position(2, 3) -> Fire,
          Position(2, 4) -> Fire,
          Position(3, 3) -> Fire,
          Position(3, 4) -> Fire
        ),
        Map(
          Position(1, 0) -> Fire,
          Position(1, 1) -> Fire,
          Position(2, 0) -> Fire,
          Position(2, 1) -> Fire
        ),
        Map(
          Position(3, 1) -> Fire,
          Position(3, 2) -> Fire,
          Position(4, 1) -> Fire,
          Position(4, 2) -> Fire
        )
      )

    "provide the correct choices to resolve the flare up card" in:
      val pattern = MediumEffect(Map("a" -> Fire)).compilePattern
      val engine = buildEngine(pattern)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(Position(3, 0) -> Fire, Position(3, 1) -> Fire, Position(3, 2) -> Fire),
        Map(Position(3, 1) -> Fire, Position(3, 2) -> Fire, Position(3, 3) -> Fire),
        Map(Position(1, 1) -> Fire, Position(2, 1) -> Fire, Position(3, 1) -> Fire),
        Map(Position(2, 1) -> Fire, Position(3, 1) -> Fire, Position(4, 1) -> Fire),
        Map(Position(3, 2) -> Fire, Position(4, 2) -> Fire),
        Map(Position(2, 1) -> Fire, Position(2, 0) -> Fire),
        Map(Position(1, 2) -> Fire, Position(0, 2) -> Fire),
        Map(Position(1, 2) -> Fire, Position(1, 3) -> Fire, Position(1, 4) -> Fire),
        Map(Position(3, 2) -> Fire, Position(3, 3) -> Fire, Position(3, 4) -> Fire),
        Map(Position(0, 1) -> Fire, Position(1, 1) -> Fire, Position(2, 1) -> Fire),
        Map(Position(2, 3) -> Fire, Position(2, 4) -> Fire),
        Map(Position(1, 1) -> Fire, Position(1, 2) -> Fire, Position(1, 3) -> Fire),
        Map(Position(1, 3) -> Fire, Position(2, 3) -> Fire, Position(3, 3) -> Fire),
        Map(Position(1, 0) -> Fire, Position(1, 1) -> Fire, Position(1, 2) -> Fire),
        Map(Position(0, 3) -> Fire, Position(1, 3) -> Fire, Position(2, 3) -> Fire),
        Map(Position(2, 3) -> Fire, Position(3, 3) -> Fire, Position(4, 3) -> Fire)
      )

    "provide the correct choices to resolve the first phase of the ember card" in:
      val goal = Rule("ember_first_phase")
      val pattern = VerySmallEffect(Map("a" -> Empty)).compilePattern
      val engine = buildEngine(pattern, tokens = Map(Position(2, 3) -> Fire))
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(Map(Position(2, 3) -> Empty))

    "provide the correct choices to resolve the second phase of the ember card" in:
      val pattern = VerySmallEffect(Map("a" -> Fire)).compilePattern
      val engine = buildEngine(pattern)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(Position(1, 2) -> Fire),
        Map(Position(2, 1) -> Fire),
        Map(Position(2, 3) -> Fire),
        Map(Position(3, 2) -> Fire)
      )

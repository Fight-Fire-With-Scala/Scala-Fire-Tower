package it.unibo.model.prolog

import it.unibo.model.effect.{LargeEffect, MediumEffect, VeryLargeEffect}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Water}
import it.unibo.model.gameboard.grid.Position
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WaterCardRulesSpec extends AbstractPrologSpec:
  "A water card" should:
    val defaultWaterTokens = Map(Position(1, 2) -> Fire)

    "provide the correct choices to resolve the smoke jumper card" in:
      val goal = Rule("smoke_jumper")
      val pattern = VeryLargeEffect(Map("a" -> Water, "b" -> Fire)).compilePattern
      val engine = buildEngine(pattern, defaultWaterTokens)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(Map(
        Position(2, 2) -> Water,
        Position(2, 1) -> Water,
        Position(2, 3) -> Water,
        Position(0, 3) -> Water,
        Position(0, 2) -> Water,
        Position(1, 2) -> Fire,
        Position(1, 1) -> Water, // Ignore since there is no fire token as in the other cases
        Position(1, 3) -> Water,
        Position(0, 1) -> Water
      ))

    val goal = Rule("water")

    "provide the correct choices to resolve the fire engine card" in:
      val pattern = LargeEffect(Map("a" -> Water)).compilePattern
      val engine = buildEngine(pattern, defaultWaterTokens)
      val sol = engine.solveAsPatterns(goal)

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

    "provide the correct choices to resolve the air drop card" in:
      val pattern = MediumEffect(Map("a" -> Water)).compilePattern
      val engine = buildEngine(pattern, defaultWaterTokens)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(Position(1, 2) -> Water, Position(1, 1) -> Water, Position(1, 0) -> Water),
        Map(Position(0, 2) -> Water, Position(1, 2) -> Water, Position(2, 2) -> Water),
        Map(Position(1, 2) -> Water, Position(1, 3) -> Water, Position(1, 4) -> Water),
        Map(Position(1, 1) -> Water, Position(1, 2) -> Water, Position(1, 3) -> Water),
        Map(Position(1, 2) -> Water, Position(0, 2) -> Water),
        Map(Position(1, 2) -> Water, Position(2, 2) -> Water, Position(3, 2) -> Water)
      )

    "provide the correct choices to resolve the bucket card" in:
      val goal = Rule("bucket")
      val pattern = MediumEffect(Map("a" -> Water)).compilePattern
      val engine = buildEngine(pattern, tokens = Map(Position(0, 0) -> Fire))
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(Position(0, 0) -> Water),
        Map(Position(0, 0) -> Water, Position(1, 0) -> Water, Position(2, 0) -> Water),
        Map(Position(0, 0) -> Water, Position(0, 1) -> Water, Position(0, 2) -> Water),
        Map(Position(0, 1) -> Water, Position(0, 0) -> Water),
        Map(Position(0, 2) -> Water, Position(0, 1) -> Water, Position(0, 0) -> Water),
        Map(Position(1, 0) -> Water, Position(0, 0) -> Water),
        Map(Position(2, 0) -> Water, Position(1, 0) -> Water, Position(0, 0) -> Water)
      )

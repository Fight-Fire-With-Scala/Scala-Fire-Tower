package it.unibo.model.prolog

import it.unibo.model.effect.{MediumAltEffect, SmallEffect, VerySmallEffect}
import it.unibo.model.gameboard.grid.ConcreteToken.{Empty, Fire, Firebreak}
import it.unibo.model.gameboard.grid.Position

class FirebreakCardRulesSpec extends AbstractPrologSpec:
  "A firebreak card" should:
    "provide the correct choices to resolve the deforest option" in:
      val goal = Rule("deforest")
      val pattern = VerySmallEffect(Map("a" -> Firebreak)).compilePattern
      val engine = buildEngine(pattern, tokens = Map(Position(1, 2) -> Fire))

      val solDeforest = engine.solveAsPatterns(goal)
      solDeforest shouldEqual Set(
        Map(Position(3, 3) -> Firebreak),
        Map(Position(1, 3) -> Firebreak),
        Map(Position(2, 0) -> Firebreak),
        Map(Position(3, 0) -> Firebreak),
        Map(Position(0, 2) -> Firebreak),
        Map(Position(1, 0) -> Firebreak),
        Map(Position(4, 1) -> Firebreak),
        Map(Position(4, 3) -> Firebreak),
        Map(Position(3, 1) -> Firebreak),
        Map(Position(3, 2) -> Firebreak),
        Map(Position(3, 4) -> Firebreak),
        Map(Position(1, 1) -> Firebreak),
        Map(Position(0, 1) -> Firebreak),
        Map(Position(2, 1) -> Firebreak),
        Map(Position(2, 3) -> Firebreak),
        Map(Position(4, 2) -> Firebreak),
        Map(Position(2, 4) -> Firebreak),
        Map(Position(1, 4) -> Firebreak),
        Map(Position(0, 3) -> Firebreak)
      )

    "provide the correct choices to resolve the reforest option" in:
      val goalDeforest = Rule("reforest")
      val pattern = VerySmallEffect(Map("a" -> Empty)).compilePattern
      val engine = buildEngine(pattern, tokens = Map(Position(1, 2) -> Firebreak))

      val solReforest = engine.solveAsPatterns(goalDeforest)
      solReforest shouldEqual Set(Map(Position(1, 2) -> Empty))

    "provide the correct choices to resolve the dozer line card" in:
      val goal = Rule("dozer_line")
      val pattern = SmallEffect(Map("a" -> Firebreak)).compilePattern
      val engine = buildEngine(pattern)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(Position(2, 4) -> Firebreak, Position(3, 4) -> Firebreak),
        Map(Position(0, 2) -> Firebreak, Position(1, 2) -> Firebreak),
        Map(Position(3, 2) -> Firebreak, Position(3, 3) -> Firebreak),
        Map(Position(2, 0) -> Firebreak),
        Map(Position(2, 3) -> Firebreak, Position(3, 3) -> Firebreak),
        Map(Position(3, 0) -> Firebreak),
        Map(Position(3, 2) -> Firebreak, Position(4, 2) -> Firebreak),
        Map(Position(1, 0) -> Firebreak, Position(1, 1) -> Firebreak),
        Map(Position(0, 1) -> Firebreak, Position(0, 2) -> Firebreak),
        Map(Position(3, 1) -> Firebreak, Position(4, 1) -> Firebreak),
        Map(Position(4, 3) -> Firebreak),
        Map(Position(3, 3) -> Firebreak, Position(4, 3) -> Firebreak),
        Map(Position(1, 3) -> Firebreak, Position(2, 3) -> Firebreak),
        Map(Position(4, 2) -> Firebreak, Position(4, 3) -> Firebreak),
        Map(Position(0, 2) -> Firebreak),
        Map(Position(1, 2) -> Firebreak, Position(1, 3) -> Firebreak),
        Map(Position(0, 3) -> Firebreak, Position(1, 3) -> Firebreak),
        Map(Position(1, 0) -> Firebreak),
        Map(Position(2, 1) -> Firebreak, Position(3, 1) -> Firebreak),
        Map(Position(0, 1) -> Firebreak, Position(1, 1) -> Firebreak),
        Map(Position(2, 0) -> Firebreak, Position(2, 1) -> Firebreak),
        Map(Position(1, 0) -> Firebreak, Position(2, 0) -> Firebreak),
        Map(Position(1, 4) -> Firebreak, Position(2, 4) -> Firebreak),
        Map(Position(4, 1) -> Firebreak),
        Map(Position(1, 1) -> Firebreak, Position(1, 2) -> Firebreak),
        Map(Position(1, 3) -> Firebreak, Position(1, 4) -> Firebreak),
        Map(Position(1, 1) -> Firebreak, Position(2, 1) -> Firebreak),
        Map(Position(0, 2) -> Firebreak, Position(0, 3) -> Firebreak),
        Map(Position(2, 3) -> Firebreak, Position(2, 4) -> Firebreak),
        Map(Position(3, 4) -> Firebreak),
        Map(Position(0, 1) -> Firebreak),
        Map(Position(3, 3) -> Firebreak, Position(3, 4) -> Firebreak),
        Map(Position(4, 2) -> Firebreak),
        Map(Position(2, 4) -> Firebreak),
        Map(Position(2, 0) -> Firebreak, Position(3, 0) -> Firebreak),
        Map(Position(1, 4) -> Firebreak),
        Map(Position(0, 3) -> Firebreak),
        Map(Position(3, 1) -> Firebreak, Position(3, 2) -> Firebreak),
        Map(Position(4, 1) -> Firebreak, Position(4, 2) -> Firebreak),
        Map(Position(3, 0) -> Firebreak, Position(3, 1) -> Firebreak)
      )

    "provide the correct choices to resolve the scratch line card" in:
      val goal = Rule("scratch_line")
      val pattern = MediumAltEffect(Map("a" -> Firebreak, "b" -> Empty)).compilePattern
      val engine = buildEngine(pattern)
      val sol = engine.solveAsPatterns(goal)

      sol shouldEqual Set(
        Map(Position(2, 1) -> Firebreak, Position(2, 0) -> Empty),
        Map(Position(0, 1) -> Firebreak, Position(0, 2) -> Empty, Position(0, 3) -> Firebreak),
        Map(Position(0, 1) -> Firebreak, Position(1, 1) -> Empty, Position(2, 1) -> Firebreak),
        Map(Position(1, 1) -> Firebreak, Position(1, 0) -> Empty),
        Map(Position(1, 3) -> Firebreak, Position(2, 3) -> Empty, Position(3, 3) -> Firebreak),
        Map(Position(4, 3) -> Firebreak),
        Map(Position(1, 3) -> Firebreak, Position(0, 3) -> Empty),
        Map(Position(3, 4) -> Firebreak),
        Map(Position(3, 1) -> Firebreak, Position(3, 0) -> Empty),
        Map(Position(2, 0) -> Firebreak),
        Map(Position(3, 1) -> Firebreak, Position(4, 1) -> Empty),
        Map(Position(3, 2) -> Firebreak, Position(4, 2) -> Empty),
        Map(Position(3, 3) -> Firebreak, Position(3, 4) -> Empty),
        Map(Position(1, 4) -> Firebreak, Position(2, 4) -> Empty, Position(3, 4) -> Firebreak),
        Map(Position(3, 0) -> Firebreak, Position(3, 1) -> Empty, Position(3, 2) -> Firebreak),
        Map(Position(1, 1) -> Firebreak, Position(2, 1) -> Empty, Position(3, 1) -> Firebreak),
        Map(Position(3, 0) -> Firebreak),
        Map(Position(3, 2) -> Firebreak, Position(3, 3) -> Empty, Position(3, 4) -> Firebreak),
        Map(Position(0, 2) -> Firebreak),
        Map(Position(4, 1) -> Firebreak, Position(4, 2) -> Empty, Position(4, 3) -> Firebreak),
        Map(Position(3, 3) -> Firebreak, Position(4, 3) -> Empty),
        Map(Position(1, 0) -> Firebreak),
        Map(Position(1, 0) -> Firebreak, Position(2, 0) -> Empty, Position(3, 0) -> Firebreak),
        Map(Position(1, 0) -> Firebreak, Position(1, 1) -> Empty, Position(1, 2) -> Firebreak),
        Map(Position(0, 3) -> Firebreak, Position(1, 3) -> Empty, Position(2, 3) -> Firebreak),
        Map(Position(1, 2) -> Firebreak, Position(0, 2) -> Empty),
        Map(Position(4, 1) -> Firebreak),
        Map(Position(2, 1) -> Firebreak, Position(3, 1) -> Empty, Position(4, 1) -> Firebreak),
        Map(Position(2, 3) -> Firebreak, Position(2, 4) -> Empty),
        Map(Position(1, 1) -> Firebreak, Position(1, 2) -> Empty, Position(1, 3) -> Firebreak),
        Map(Position(3, 1) -> Firebreak, Position(3, 2) -> Empty, Position(3, 3) -> Firebreak),
        Map(Position(0, 1) -> Firebreak),
        Map(Position(2, 3) -> Firebreak, Position(3, 3) -> Empty, Position(4, 3) -> Firebreak),
        Map(Position(4, 2) -> Firebreak),
        Map(Position(2, 4) -> Firebreak),
        Map(Position(1, 4) -> Firebreak),
        Map(Position(0, 3) -> Firebreak),
        Map(Position(1, 3) -> Firebreak, Position(1, 4) -> Empty),
        Map(Position(1, 2) -> Firebreak, Position(1, 3) -> Empty, Position(1, 4) -> Firebreak),
        Map(Position(1, 1) -> Firebreak, Position(0, 1) -> Empty)
      )

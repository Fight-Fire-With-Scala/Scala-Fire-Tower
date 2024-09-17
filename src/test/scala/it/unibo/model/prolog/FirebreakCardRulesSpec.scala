package it.unibo.model.prolog

import it.unibo.model.effect.card.FirebreakEffect
import it.unibo.model.gameboard.grid.ConcreteToken.{ Empty, Firebreak }
import it.unibo.model.prolog.AbstractCardSolverSpec.given_Conversion_ICardEffect_ILogicComputation
import it.unibo.model.gameboard.grid.Position

class FirebreakCardRulesSpec extends AbstractCardSolverSpec:
  "A firebreak card" should:
//    "provide the correct choices to solve the deforest option" in:
//      val engine = buildEngine(Map(dummyCardId -> List(FirebreakEffect.DeReforest)))
//      val sol    = engine.solveAsPatterns(FirebreakEffect.ScratchLine.goals.last(dummyCardId))
//      sol shouldEqual Set(
//        Map(Position(2, 3) -> Firebreak),
//        Map(Position(3, 3) -> Firebreak),
//        Map(Position(4, 2) -> Firebreak),
//        Map(Position(3, 4) -> Firebreak),
//        Map(Position(2, 1) -> Firebreak),
//        Map(Position(3, 2) -> Firebreak),
//        Map(Position(0, 1) -> Firebreak),
//        Map(Position(3, 0) -> Firebreak),
//        Map(Position(2, 0) -> Firebreak),
//        Map(Position(1, 1) -> Firebreak),
//        Map(Position(1, 3) -> Firebreak),
//        Map(Position(4, 3) -> Firebreak),
//        Map(Position(1, 0) -> Firebreak),
//        Map(Position(0, 2) -> Firebreak),
//        Map(Position(0, 3) -> Firebreak),
//        Map(Position(2, 4) -> Firebreak),
//        Map(Position(4, 1) -> Firebreak),
//        Map(Position(3, 1) -> Firebreak),
//        Map(Position(1, 4) -> Firebreak)
//      )

//    "provide the correct choices to solve the reforest option" in:
//      val engine = buildEngine(Map(dummyCardId -> List(FirebreakEffect.DeReforest)))
//      val sol = engine.solveAsPatterns(FirebreakEffect.ScratchLine.goals.head(dummyCardId))
//      sol shouldEqual Set(Map(Position(1, 2) -> Empty))

    "provide the correct choices to solve the dozer line card" in:
      val sol = getAvailablePatterns(FirebreakEffect.DozerLine)
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

    "provide the correct choices to solve the scratch line card" in:
      val sol = getAvailablePatterns(FirebreakEffect.ScratchLine)
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

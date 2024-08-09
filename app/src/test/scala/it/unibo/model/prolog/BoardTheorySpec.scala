package it.unibo.model.prolog

import alice.tuprolog.Theory
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.{BasicGrid, Cell, Position}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import it.unibo.model.prolog.PrologUtils.given

@RunWith(classOf[JUnitRunner])
class BoardTheorySpec extends AbstractPrologSpec:
  private val expectedBoard: Theory = dummyBoard(directions, deltas)

  "Board Theory" should:
    "build a correct board" in:
      val cells: Map[Position, Cell] = Map(
        Position(0, 0) -> Tower,
        Position(0, 1) -> Woods,
        Position(0, 2) -> Woods,
        Position(0, 3) -> Woods,
        Position(0, 4) -> Tower,
        Position(1, 0) -> Woods,
        Position(1, 1) -> Woods,
        Position(1, 2) -> Woods,
        Position(1, 3) -> Woods,
        Position(1, 4) -> Woods,
        Position(2, 0) -> Woods,
        Position(2, 1) -> Woods,
        Position(2, 2) -> EternalFire,
        Position(2, 3) -> Woods,
        Position(2, 4) -> Woods,
        Position(3, 0) -> Woods,
        Position(3, 1) -> Woods,
        Position(3, 2) -> Woods,
        Position(3, 3) -> Woods,
        Position(3, 4) -> Woods,
        Position(4, 0) -> Tower,
        Position(4, 1) -> Woods,
        Position(4, 2) -> Woods,
        Position(4, 3) -> Woods,
        Position(4, 4) -> Tower
      )
      val tokens = Map(Position(0, 0) -> Fire, Position(0, 1) -> Fire)
      val grid = BasicGrid(cells, tokens)
      val pattern = Map(Position(0, 0) -> Fire, Position(0, 1) -> Fire)
      val directions = Direction.values.toList

      val board: Theory = BoardTheory(grid, pattern, directions)

      board shouldEqual expectedBoard

package it.unibo.model.prolog

import alice.tuprolog.{Struct, Theory}
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.{BasicGrid, Cell, Position}
import it.unibo.model.prolog.PrologUtils.given
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardTheorySpec extends AnyWordSpecLike with Matchers:
  val defaultDirections: List[String] = Direction.values.map(_.getId).toList
  val defaultDeltas: List[Struct] = Direction.values.map(_.getDelta)
    .map(d => Struct.tuple(d.x, d.y)).toList

  val dummyBoard: (List[String], List[Struct]) => Theory = (directions, deltas) =>
    Theory.fromPrologList(Struct.list(
      // Cells
      Struct.of("cell", Struct.tuple(0, 0), Tower),
      Struct.of("cell", Struct.tuple(0, 1), Woods),
      Struct.of("cell", Struct.tuple(0, 2), Woods),
      Struct.of("cell", Struct.tuple(0, 3), Woods),
      Struct.of("cell", Struct.tuple(0, 4), Tower),
      Struct.of("cell", Struct.tuple(1, 0), Woods),
      Struct.of("cell", Struct.tuple(1, 1), Woods),
      Struct.of("cell", Struct.tuple(1, 2), Woods),
      Struct.of("cell", Struct.tuple(1, 3), Woods),
      Struct.of("cell", Struct.tuple(1, 4), Woods),
      Struct.of("cell", Struct.tuple(2, 0), Woods),
      Struct.of("cell", Struct.tuple(2, 1), Woods),
      Struct.of("cell", Struct.tuple(2, 2), EternalFire),
      Struct.of("cell", Struct.tuple(2, 3), Woods),
      Struct.of("cell", Struct.tuple(2, 4), Woods),
      Struct.of("cell", Struct.tuple(3, 0), Woods),
      Struct.of("cell", Struct.tuple(3, 1), Woods),
      Struct.of("cell", Struct.tuple(3, 2), Woods),
      Struct.of("cell", Struct.tuple(3, 3), Woods),
      Struct.of("cell", Struct.tuple(3, 4), Woods),
      Struct.of("cell", Struct.tuple(4, 0), Tower),
      Struct.of("cell", Struct.tuple(4, 1), Woods),
      Struct.of("cell", Struct.tuple(4, 2), Woods),
      Struct.of("cell", Struct.tuple(4, 3), Woods),
      Struct.of("cell", Struct.tuple(4, 4), Tower),

      // Tokens
      Struct.of("token", Struct.tuple(0, 0), Fire),
      Struct.of("token", Struct.tuple(0, 1), Fire),

      // Grid size
      Struct.of("numRows", 5),
      Struct.of("numCols", 5),

      // Patterns
      Struct.of("pattern", Struct.tuple(0, 0), Fire),
      Struct.of("pattern", Struct.tuple(0, 1), Fire),

      Struct.of("directions", directions),
      Struct.of("deltas", deltas)
    ))
  val expectedBoard: Theory = dummyBoard(defaultDirections, defaultDeltas)

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

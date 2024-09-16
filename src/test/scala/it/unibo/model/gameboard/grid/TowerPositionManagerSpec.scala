package it.unibo.model.gameboard.grid

import it.unibo.model.gameboard.grid.Cell.Tower
import it.unibo.model.gameboard.grid.TowerPosition._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TowerPositionManagerSpec extends AnyWordSpec with Matchers:

  "TowerPositionManager" should:
    "correctly identify tower cells in the top-left diagonal" in:
      val cells = Map(
        Position(0, 0) -> Tower,
        Position(1, 1) -> Tower,
        Position(2, 2) -> Tower,
        Position(8, 8) -> Tower
      )
      val towerPositions = Set(TOP_LEFT)
      val result = TowerPositionManager.getTowerCells(cells, towerPositions)
      result shouldBe Set(Position(0, 0), Position(1, 1), Position(2, 2))

    "correctly identify tower cells in the bottom-right diagonal" in:
      val cells = Map(
        Position(15, 15) -> Tower,
        Position(14, 14) -> Tower,
        Position(13, 13) -> Tower,
        Position(0, 0) -> Tower
      )
      val towerPositions = Set(BOTTOM_RIGHT)
      val result = TowerPositionManager.getTowerCells(cells, towerPositions)
      result shouldBe Set(Position(15, 15), Position(14, 14), Position(13, 13))

    "correctly identify tower cells in the top-right diagonal" in:
      val cells = Map(
        Position(0, 15) -> Tower,
        Position(1, 14) -> Tower,
        Position(2, 13) -> Tower,
        Position(8, 8) -> Tower
      )
      val towerPositions = Set(TOP_RIGHT)
      val result = TowerPositionManager.getTowerCells(cells, towerPositions)
      result shouldBe Set(Position(0, 15), Position(1, 14), Position(2, 13))

    "correctly identify tower cells in the bottom-left diagonal" in:
      val cells = Map(
        Position(15, 0) -> Tower,
        Position(14, 1) -> Tower,
        Position(13, 2) -> Tower,
        Position(8, 8) -> Tower
      )
      val towerPositions = Set(BOTTOM_LEFT)
      val result = TowerPositionManager.getTowerCells(cells, towerPositions)
      result shouldBe Set(Position(15, 0), Position(14, 1), Position(13, 2))
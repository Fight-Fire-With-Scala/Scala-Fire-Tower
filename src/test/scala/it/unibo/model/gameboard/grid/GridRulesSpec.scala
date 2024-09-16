package it.unibo.model.gameboard.grid

import it.unibo.model.gameboard.grid.Cell.{EternalFire, Tower, Woods}
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Firebreak, Reforest, Water}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridRulesSpec extends AnyWordSpec with Matchers:

  "A grid" should:
    "not place a token on EternalFire" in:
      val grid = Grid.empty.setCell(Position(0, 0), EternalFire)
      val updatedGrid = grid.setToken(Position(0, 0), Fire)
      updatedGrid.getToken(Position(0, 0)) shouldBe None

    "handle placing Fire on Woods correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods)
      val updatedGrid = grid.setToken(Position(0, 0), Fire)
      updatedGrid.getToken(Position(0, 0)) shouldBe Some(Fire)

    "handle placing Water on Woods with Fire correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setToken(Position(0, 0), Fire)
      val updatedGrid = grid.setToken(Position(0, 0), Water)
      updatedGrid.getToken(Position(0, 0)) shouldBe None

    "handle placing Firebreak on Woods with Fire correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setToken(Position(0, 0), Fire)
      val updatedGrid = grid.setToken(Position(0, 0), Firebreak)
      updatedGrid.getToken(Position(0, 0)) shouldBe Some(Firebreak)

    "handle placing Reforest on Woods with Firebreak correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setToken(Position(0, 0), Firebreak)
      val updatedGrid = grid.setToken(Position(0, 0), Reforest)
      updatedGrid.getToken(Position(0, 0)) shouldBe None

    "handle placing Water on Tower correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Tower)
      val updatedGrid = grid.setToken(Position(0, 0), Water)
      updatedGrid.getToken(Position(0, 0)) shouldBe None

    "handle placing Fire on Tower correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Tower)
      val updatedGrid = grid.setToken(Position(0, 0), Fire)
      updatedGrid.getToken(Position(0, 0)) shouldBe Some(Fire)

    "handle placing Reforest on Tower with Firebreak correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Tower).setToken(Position(0, 0), Firebreak)
      val updatedGrid = grid.setToken(Position(0, 0), Reforest)
      updatedGrid.getToken(Position(0, 0)) shouldBe None

    "not change the grid when placing Reforest on a non-Firebreak token cell" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setToken(Position(0, 0), Fire)
      val updatedGrid = grid.setToken(Position(0, 0), Reforest)
      updatedGrid.cells shouldBe grid.cells
      updatedGrid.tokens shouldBe grid.tokens

    "handle placing tokens at the edges and corners of the grid" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setCell(Position(15, 15), Woods)
      val updatedGrid = grid.setToken(Position(0, 0), Fire).setToken(Position(15, 15), Fire)
      updatedGrid.getToken(Position(0, 0)) shouldBe Some(Fire)
      updatedGrid.getToken(Position(15, 15)) shouldBe Some(Fire)

    "not place tokens at invalid positions" in:
      val grid = Grid.empty
      an [IllegalArgumentException] should be thrownBy grid.setToken(Position(-1, -1), Fire)
      an [IllegalArgumentException] should be thrownBy grid.setToken(Position(16, 16), Fire)

    "handle placing tokens on empty cells correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods)
      val updatedGrid = grid.setToken(Position(0, 0), Fire)
      updatedGrid.getToken(Position(0, 0)) shouldBe Some(Fire)

    "handle replacing existing tokens with new ones correctly" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setToken(Position(0, 0), Fire)
      val updatedGrid = grid.setToken(Position(0, 0), Water)
      updatedGrid.getToken(Position(0, 0)) shouldBe None

    "handle complex scenarios involving multiple token placements and removals" in:
      val grid = Grid.empty.setCell(Position(0, 0), Woods).setCell(Position(1, 1), Woods)
      val updatedGrid = grid.setToken(Position(0, 0), Fire).setToken(Position(1, 1), Firebreak)
      updatedGrid.getToken(Position(0, 0)) shouldBe Some(Fire)
      updatedGrid.getToken(Position(1, 1)) shouldBe Some(Firebreak)
      val finalGrid = updatedGrid.setToken(Position(0, 0), Water).setToken(Position(1, 1), Reforest)
      finalGrid.getToken(Position(0, 0)) shouldBe None
      finalGrid.getToken(Position(1, 1)) shouldBe None
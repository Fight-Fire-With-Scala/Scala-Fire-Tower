package it.unibo.model.gameboard.grid

import it.unibo.model.gameboard.grid.{Cell, Grid, Position}
import it.unibo.model.gameboard.grid.Cell.*
import it.unibo.model.gameboard.grid.GridBuilder.DSL.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GridSpec extends AnyWordSpec with Matchers:

  def assertCellType(position: Position, expectedType: Cell, grid: Grid): Unit =
    val cell = grid.getCell(position).get
    expectedType match
      case _ if expectedType == Tower       => cell shouldBe a[Tower.type]
      case _ if expectedType == EternalFire => cell shouldBe a[EternalFire.type]
      case _ if expectedType == Woods       => cell shouldBe a[Woods.type]
      case _                                => fail(s"Unexpected cell type: $expectedType")

  val expectedCells: List[(Position, Cell)] = List(
    (Position(0, 0), Tower),
    (Position(7, 7), EternalFire),
    (Position(8, 8), EternalFire),
    (Position(5, 5), Woods)
  )

  "An empty grid" should:
    "have no cells" in:
      val grid = Grid.empty
      grid.cells shouldBe empty

  "A grid" should:
    "correctly add and retrieve a cell" in:
      val initialGrid = Grid.empty
      val position = Position(0, 0)
      val cell = Tower
      val updatedGrid = initialGrid.setCell(position, cell)
      updatedGrid.getCell(position) shouldBe Some(cell)

    "update a cell correctly" in:
      val position = Position(1, 1)
      val initialCell = Woods
      val updatedCell = Woods
      val gridWithInitialCell = Grid.empty.setCell(position, initialCell)
      val gridWithUpdatedCell = gridWithInitialCell.setCell(position, updatedCell)
      gridWithUpdatedCell.getCell(position) shouldBe Some(updatedCell)

  // noinspection ScalaUnusedExpression
  "A standard grid" should:
    "be initialized with the correct pattern" in:
      val standardGrid = Grid.standard
      standardGrid.cells.size shouldBe Grid.positionNumber

      expectedCells.foreach { case (position, cellType) =>
        assertCellType(position, cellType, Grid.standard)
      }

    "maintain the correct size after operations" in:
      val grid = Grid.standard
      val position = Position(3, 3)
      val cell = EternalFire
      val updatedGrid = grid.setCell(position, cell)
      updatedGrid.cells.size shouldBe Grid.positionNumber

    // noinspection ScalaUnusedExpression
    "be initialized with the correct pattern using the DSL" in:
      val standardGrid = Grid {
        T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
        T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
        T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | E | E | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | E | E | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        F | F | F | F | F | F | F | F | F | F | F | F | F | F | F | F
        T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
        T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
        T | T | T | F | F | F | F | F | F | F | F | F | F | T | T | T
      }

      standardGrid.cells.size shouldBe Grid.positionNumber

      expectedCells.foreach { case (position, cellType) =>
        assertCellType(position, cellType, Grid.standard)
      }

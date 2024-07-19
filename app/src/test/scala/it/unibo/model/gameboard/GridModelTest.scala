package it.unibo.model.gameboard

import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner
import it.unibo.model.grid.{Cell, EternalFire, Grid, Position, Tower, Woods}
import it.unibo.model.grid.GridBuilder.DSL.*

@RunWith(classOf[JUnitRunner])
class GridModelTest extends AnyWordSpec with Matchers:

  def assertCellType(position: Position, expectedType: Class[?], grid: Grid): Unit =
    val cell = grid.getCell(position).get
    expectedType match
      case _ if expectedType == classOf[Tower]       => cell shouldBe a[Tower]
      case _ if expectedType == classOf[EternalFire] => cell shouldBe a[EternalFire]
      case _ if expectedType == classOf[Woods]       => cell shouldBe a[Woods]
      case _                                         => fail(s"Unexpected cell type: $expectedType")

  val expectedCells: Seq[(Position, Class[? >: Tower & EternalFire & Woods <: Cell])] = List(
    (Position(0, 0), classOf[Tower]),
    (Position(7, 7), classOf[EternalFire]),
    (Position(8, 8), classOf[EternalFire]),
    (Position(5, 5), classOf[Woods])
  )

  "An empty grid" should:
    "have no cells" in:
      val grid = Grid.empty
      grid.cells shouldBe empty

  "A grid" should:
    "correctly add and retrieve a cell" in:
      val initialGrid = Grid.empty
      val position = Position(0, 0)
      val cell = Tower()
      val updatedGrid = initialGrid.setCell(position, cell)
      updatedGrid.getCell(position) shouldBe Some(cell)

    "update a cell correctly" in:
      val position = Position(1, 1)
      val initialCell = Woods()
      val updatedCell = Woods()
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
      val cell = EternalFire()
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

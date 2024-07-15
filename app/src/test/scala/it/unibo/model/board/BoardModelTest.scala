package it.unibo.model.board

import it.unibo.model.cells.*
import org.junit.runner.RunWith
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.junit.JUnitRunner
import it.unibo.model.Position

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyWordSpec with Matchers:

  "An empty board" should:
    "have no cells" in:
      val board = Board.empty
      board.cells shouldBe empty

  "A board" should:
    "correctly add and retrieve a cell" in:
      val initialBoard = Board.empty
      val position = Position(0, 0)
      val cell = Tower()
      val updatedBoard = initialBoard.setCell(position, cell)
      updatedBoard.getCell(position) shouldBe Some(cell)

    "update a cell correctly" in:
      val position = Position(1, 1)
      val initialCell = Woods()
      val updatedCell = Woods()
      val boardWithInitialCell = Board.empty.setCell(position, initialCell)
      val boardWithUpdatedCell = boardWithInitialCell.setCell(position, updatedCell)
      boardWithUpdatedCell.getCell(position) shouldBe Some(updatedCell)

  // noinspection ScalaUnusedExpression
  "A standard board" should:
    "be initialized with the correct pattern" in:
      val standardBoard = Board.standard
      standardBoard.cells.size shouldBe Board.positionNumber

      standardBoard.getCell(Position(0, 0)).get shouldBe a[Tower]
      standardBoard.getCell(Position(7, 7)).get shouldBe an[EternalFire]

    "maintain the correct size after operations" in:
      val board = Board.standard
      val position = Position(3, 3)
      val cell = EternalFire()
      val updatedBoard = board.setCell(position, cell)
      updatedBoard.cells.size shouldBe Board.positionNumber

    // noinspection ScalaUnusedExpression
    "be initialized with the correct pattern using the DSL" in:
      val standardBoard = Board {
        import BoardBuilder.DSL.*
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

      standardBoard.cells.size shouldBe Board.positionNumber
      // Example test to verify a specific cell type at a given position
      standardBoard.getCell(Position(0, 0)).get shouldBe a[Tower]
      standardBoard.getCell(Position(7, 7)).get shouldBe a[EternalFire]
      standardBoard.getCell(Position(8, 8)).get shouldBe a[EternalFire]
      standardBoard.getCell(Position(5, 5)).get shouldBe a[Woods]

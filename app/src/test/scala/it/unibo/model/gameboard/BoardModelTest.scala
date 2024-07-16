package it.unibo.model.gameboard

import it.unibo.model.board.Board
import org.junit.runner.RunWith
import it.unibo.model.grid.Grid
import it.unibo.model.cards.resolvers.wind.WindDirection
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyFlatSpec with Matchers {

  "A Board with Random Wind and Standard Grid" should "initialize with a valid wind direction and a standard grid" in {
    val board = Board.withRandomWindAndStandardGrid
    board.grid shouldBe a [Grid]
    board.grid.cells.size shouldBe Grid.positionNumber
    Seq(WindDirection.North, WindDirection.South, WindDirection.East, WindDirection.West) should contain (board.windDirection)
  }

}
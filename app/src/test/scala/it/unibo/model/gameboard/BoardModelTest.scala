package it.unibo.model.gameboard

import it.unibo.model.board.Board
import it.unibo.model.cards.effects.WindCard
import it.unibo.model.cards.{Card, CardType, Deck}
import org.junit.runner.RunWith
import it.unibo.model.grid.Grid
import it.unibo.model.cards.resolvers.wind.{WindChoice, WindDirection}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyFlatSpec with Matchers:

  // noinspection ScalaUnusedExpression
  "A Board with Random Wind and Standard Grid" should
    "initialize with a valid wind direction and a standard grid" in {
      val board = Board.withRandomWindAndStandardGrid
      board.grid shouldBe a[Grid]
      board.grid.cells.size shouldBe Grid.positionNumber
      WindDirection.windDirections should contain(board.windDirection)
    }

  "Playing a card with Wind effect" should "update the wind direction in the board" in {
    val initialWindDirection = WindDirection.North
    val board = Board(Grid.standard, initialWindDirection)
    val gameBoard = GameBoard(board, Deck(List.empty))

    val cardType =
      CardType(title = "Sud", description = "", amount = 2, effect = WindCard.South.effect)

    val updatedGameBoard = gameBoard
      .resolveCardPlayed(Card(0, cardType), Some(WindChoice.UpdateWind))
    updatedGameBoard.board.windDirection shouldBe WindDirection.South
  }

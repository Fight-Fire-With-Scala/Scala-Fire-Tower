package it.unibo.model.gameboard

import it.unibo.model.cards.{Card, CardType}
import it.unibo.model.cards.effects.WindEffect
import it.unibo.model.cards.types.WindCard
import it.unibo.model.cards.choices.WindChoice
import it.unibo.model.gameboard.board.Board
import it.unibo.model.gameboard.grid.Grid
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyFlatSpec with Matchers:

  def standardBoardInitialisation(): GameBoard =
    val initialWindDirection = WindEffect.North
    val board = Board(Grid.standard, initialWindDirection)
    GameBoard(board, Deck(List.empty))

  "A Board with Random Wind and Standard Grid" should
    "initialize with a valid wind direction and a standard grid" in {
      val board = Board.withRandomWindAndStandardGrid
      board.grid shouldBe a[Grid]
      board.grid.cells.size shouldBe Grid.positionNumber
      WindEffect.values should contain(board.windDirection)
    }

  "Playing a card with Wind effect and update choice" should
    "update the wind direction in the board with wind specified" in {
      val gameBoard = standardBoardInitialisation()
      val cardType =
        CardType(title = "Sud", description = "", amount = 2, effectType = WindCard.South)

      val updatedGameBoard = gameBoard.resolveCardPlayed(Card(0, cardType), WindChoice.UpdateWind)
      updatedGameBoard.board.windDirection shouldBe WindEffect.South
    }

  "Playing a card with Wind effect and random update choice" should
    "update the wind direction in the board with a random wind direction" in {
      val gameBoard = standardBoardInitialisation()
      val cardType =
        CardType(title = "Random Wind", description = "", amount = 2, effectType = WindCard.North)

      val updatedGameBoard = gameBoard
        .resolveCardPlayed(Card(0, cardType), WindChoice.RandomUpdateWind)
      WindEffect.values should contain(updatedGameBoard.board.windDirection)
    }

  "Playing a card with Wind effect and place fire choice" should
    "not update the wind direction in the board" in {
      val gameBoard = standardBoardInitialisation()
      val cardType =
        CardType(title = "Place Fire", description = "", amount = 2, effectType = WindCard.East)

      val updatedGameBoard = gameBoard.resolveCardPlayed(Card(0, cardType), WindChoice.PlaceFire)
      updatedGameBoard.board.windDirection shouldBe WindEffect.North
    }

  "Playing a card with Wind effect and multi-step resolver" should
    "apply the multi-step effect correctly" in {
      val gameBoard = standardBoardInitialisation()
      val cardType =
        CardType(title = "MultiStep Wind", description = "", amount = 2, effectType = WindCard.West)

      val updatedGameBoard =
        gameBoard
          .resolveCardPlayed(
            Card(0, cardType),
            WindChoice.PlaceFire
          ) // Assuming PlaceFire triggers multi-step resolver
      // Add assertions to verify the multi-step effect
      // Example: updatedGameBoard.board.someProperty shouldBe expectedValue
    }

  "Changing turn in a game" should "change the current player of the board" in {
    val gameBoard = standardBoardInitialisation()

    gameBoard.currentPlayer shouldBe Player1
    val updatedGameBoard = gameBoard.changeTurn()
    updatedGameBoard.currentPlayer shouldBe Player2
  }

package it.unibo.model.gameboard

import it.unibo.model.card.{Card, CardType}
import it.unibo.model.card.types.WindCard
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.gameboard.player.Person
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyFlatSpec with Matchers:

  def standardBoardInitialisation(): GameBoard =
    val initialWindDirection = Direction.North
    val board = Board(Grid.standard, initialWindDirection)
    val player1 = Person("Player1", List.empty, List.empty)
    val player2 = Person("Player2", List.empty, List.empty)
    GameBoard(board, Deck(List.empty), player1, player2, player1)

  "A Board with Random Wind and Standard Grid" should
    "initialize with a valid wind direction and a standard grid" in {
      val board = Board.withRandomWindAndStandardGrid
      board.grid shouldBe a[Grid]
      board.grid.cells.size shouldBe Grid.positionNumber
      Direction.values should contain(board.windDirection)
    }

  "Playing a card with Wind effect and update choice" should
    "update the wind direction in the board with wind specified" in {
      val gameBoard = standardBoardInitialisation()
      val cardType =
        CardType(title = "Sud", description = "", amount = 2, effectType = WindCard.South)

      val updatedGameBoard = gameBoard.resolveChoice(Card(0, cardType), WindEffect.UpdateWind)
      updatedGameBoard.board.windDirection shouldBe Direction.South
    }

  "Playing a card with Wind effect and random update choice" should
    "update the wind direction in the board with a random wind direction" in {
      val gameBoard = standardBoardInitialisation()
      val cardType =
        CardType(title = "Random Wind", description = "", amount = 2, effectType = WindCard.North)

      val updatedGameBoard = gameBoard
        .resolveChoice(Card(0, cardType), WindEffect.RandomUpdateWind)
      Direction.values should contain(updatedGameBoard.board.windDirection)
    }

//  "Playing a card with Wind effect and place fire choice" should
//    "not update the wind direction in the board" in {
//      val gameBoard = standardBoardInitialisation()
//      val cardType =
//        CardType(title = "Place Fire", description = "", amount = 2, effectType = WindCard.East)
//
//      val updatedGameBoard = gameBoard.resolveCardPlayed(Card(0, cardType), WindChoice.PlaceFire)
//      updatedGameBoard.board.windDirection shouldBe WindEffect(Direction.North)
//    }

//  "Playing a card with Wind effect and multi-step resolver" should
//    "apply the multi-step effect correctly" in {
//      val gameBoard = standardBoardInitialisation()
//      val cardType =
//        CardType(title = "MultiStep Wind", description = "", amount = 2, effectType = WindCard.West)
//
//      val updatedGameBoard =
//        gameBoard
//          .resolveCardPlayed(
//            Card(0, cardType),
//            WindChoice.PlaceFire
//          ) // Assuming PlaceFire triggers multi-step resolver
//      // Add assertions to verify the multi-step effect
//      // Example: updatedGameBoard.board.someProperty shouldBe expectedValue
//    }

//  "Changing turn in a game" should "change the current player of the board" in {
//    val gameBoard = standardBoardInitialisation()
//
//    gameBoard.currentPlayer shouldBe Player1
//    val updatedGameBoard = gameBoard.changeTurn()
//    updatedGameBoard.currentPlayer shouldBe Player2
//  }

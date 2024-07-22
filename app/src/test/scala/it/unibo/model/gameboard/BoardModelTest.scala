package it.unibo.model.gameboard

import it.unibo.model.board.Board
import it.unibo.model.cards.choices.WindChoice
import it.unibo.model.cards.effects.WindEffect
import it.unibo.model.cards.types.WindCardType
import it.unibo.model.cards.{Card, CardType}
import org.junit.runner.RunWith
import it.unibo.model.grid.Grid
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BoardModelTest extends AnyFlatSpec with Matchers:

  def standardBoardInitialisation(): GameBoard =
    val initialWindDirection = WindEffect.North
    val board = Board(Grid.standard, initialWindDirection)
    GameBoard(board, Deck(List.empty))

  // noinspection ScalaUnusedExpression
  "A Board with Random Wind and Standard Grid" should
    "initialize with a valid wind direction and a standard grid" in {
      val board = Board.withRandomWindAndStandardGrid
      board.grid shouldBe a[Grid]
      board.grid.cells.size shouldBe Grid.positionNumber
      WindEffect.windDirections should contain(board.windDirection)
    }

//  "Playing a card with Wind effect and update choice" should "update the wind direction in the board with wind specified" in {
//    val gameBoard = standardBoardInitialisation()
//    val cardType =
//      CardType(title = "Sud", description = "", amount = 2, effect = WindCardType.South.effect)
//
//    val updatedGameBoard = gameBoard
//      .resolveCardPlayed(Card(0, cardType), Some(WindChoice.UpdateWind))
//    updatedGameBoard.board.windDirection shouldBe WindDirection.South
//  }

//  "Playing a card with Wind effect and random choice" should
//    "update the wind direction in the board with one of the wind directions" in {
//      val gameBoard = standardBoardInitialisation()
//      val cardType =
//        CardType(title = "Sud", description = "", amount = 2, effect = WindCardType.South.effect)
//
//      val updatedGameBoard = gameBoard
//        .resolveCardPlayed(Card(0, cardType), Some(WindChoice.RandomUpdateWind))
//      WindEffect.windDirections should contain(updatedGameBoard.board.windDirection)
//    }

  "Changing turn in a game" should "change the current player of the board" in {
    val gameBoard = standardBoardInitialisation()

    gameBoard.currentPlayer shouldBe Player1
    val updatedGameBoard = gameBoard.changeTurn()
    updatedGameBoard.currentPlayer shouldBe Player2
  }

package it.unibo.model.gameboard

import it.unibo.model.GameBoardInitializer
import it.unibo.model.effect.card.FirebreakEffect.*
import it.unibo.model.effect.card.WindUpdateEffect.UpdateWind
import it.unibo.model.effect.pattern.PatternEffect.{ CardComputation, PatternApplication, PatternComputation }
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.{ ConcreteToken, Grid, Position, Token, TowerPosition }
import it.unibo.model.gameboard.player.{ Person, Player, PlayerManager }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterAll

import scala.compiletime.uninitialized

class GameBoardSpec
    extends AnyWordSpec
    with Matchers
    with GameBoardInitializer
    with BeforeAndAfterAll:

  var gameBoard: GameBoard = uninitialized

  override def beforeAll(): Unit =
    val player1 = Person("Player1", List.empty, List.empty)
    val player2 = Person("Player2", List.empty, List.empty)
    val gb      = GameBoard(player1, player2)
    gameBoard = initialiseGameBoardPlayers(gb, player1, player2)

  "A GameBoard" should:

    "initialize correctly with random wind and standard grid" in:
      gameBoard.board.grid shouldBe Grid.standard
      gameBoard.board.windDirection should not be null

    "return the correct current player" in:
      gameBoard.getCurrentPlayer shouldBe gameBoard.player1

    "return the correct opponent" in:
      gameBoard.getOpponent shouldBe gameBoard.player2

    "update the current player correctly" in:
      val newPlayer        = Person("PlayerNew", List.empty, List.empty)
      val updatedGameBoard = gameBoard.updateCurrentPlayer(newPlayer)
      updatedGameBoard.getCurrentPlayer shouldBe newPlayer

    "change the player correctly" in:
      val initialPlayer    = gameBoard.getCurrentPlayer
      val updatedGameBoard = gameBoard.changePlayer()
      updatedGameBoard.getCurrentPlayer should not be initialPlayer

    "identify if the game has ended correctly" in:
      val board = gameBoard.board
      val updatedGameBoard = gameBoard.copy(
        board.copy(grid =
          gameBoard.board.grid.setToken(TowerPosition.TOP_RIGHT.position, ConcreteToken.Fire)
        )
      )
      updatedGameBoard.isGameEnded shouldBe Some(gameBoard.player2)

    "solve phase effects correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(PhaseEffect(WaitingPhase))
      updatedGameBoard.gamePhase shouldBe WaitingPhase

//    "solve hand effects correctly" in:
//      val updatedGameBoard = gameBoard.solveEffect(PlayCard(12))
//      updatedGameBoard.player1. should not be None

    "solve pattern application correctly" in:
      val position: Position = Position(10, 10)
      val map: Pattern       = Map(position -> Fire)
      val updatedGameBoard   = gameBoard.solveEffect(PatternApplication(map))
      updatedGameBoard.board.grid.getToken(position) shouldBe Some(Fire)

    "solve pattern computation correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(PatternComputation(ScratchLine))
      updatedGameBoard.player1.lastPatternChosen should not be List.empty

    "solve card computation correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(CardComputation(12, ScratchLine))
      updatedGameBoard.player1.lastCardChosen should not be List.empty

    "solve wind update correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(UpdateWind(Direction.South))
      updatedGameBoard.board.windDirection shouldBe Direction.South

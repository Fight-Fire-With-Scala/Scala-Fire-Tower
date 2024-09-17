package it.unibo.model.gameboard

import it.unibo.controller.model.PlayerController
import it.unibo.model.effect.card.FireEffect.Explosion
import it.unibo.model.effect.card.FirebreakEffect.*
import it.unibo.model.effect.card.WindUpdateEffect.{RandomUpdateWind, UpdateWind}
import it.unibo.model.effect.hand.HandEffect.PlayCard
import it.unibo.model.effect.pattern.PatternEffect.{CardComputation, PatternApplication, PatternComputation}
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.{ConcreteToken, Grid, Position, Token, TowerPosition}
import it.unibo.model.gameboard.player.{Person, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterAll

import scala.compiletime.uninitialized

class GameBoardSpec extends AnyWordSpec with Matchers with PlayerController with BeforeAndAfterAll:

  var gameBoard: GameBoard = uninitialized

  override def beforeAll(): Unit =
    initialiseGameBoard()

  def initialiseGameBoard(): Unit =
    val initialGameBoard = GameBoard(Person("Player1", List.empty, List.empty), Person("Player2", List.empty, List.empty))
    // Initialize both players
    val (updatedGameBoard, updatedPlayer1) = initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)
    val (finalGameBoard, updatedPlayer2) = initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
    val completeGameBoard = finalGameBoard
      .copy(player1 = updatedPlayer1, player2 = updatedPlayer2)
    gameBoard = completeGameBoard
      .solveEffect(PhaseEffect(completeGameBoard.gamePhase))

  "A GameBoard" should:

    "initialize correctly with random wind and standard grid" in:
      gameBoard.board.grid shouldBe Grid.standard
      gameBoard.board.windDirection should not be null

    "identify if the game has ended correctly" in:
      val board     = gameBoard.board
      val updatedGameBoard = gameBoard.copy(
        board.copy(grid =
          gameBoard.board.grid.setToken(TowerPosition.TOP_RIGHT.position, ConcreteToken.Fire)
        )
      )
      updatedGameBoard.isGameEnded shouldBe Some(gameBoard.player2)

    "solve phase effects correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(PhaseEffect(WaitingPhase))
      updatedGameBoard.gamePhase shouldBe WaitingPhase

    "solve hand effects correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(PlayCard(12))
      updatedGameBoard.player1.lastCardChosen should not be None

    "solve pattern application correctly" in:
      val position: Position = Position(10, 10)
      val map: Map[Position, Token] = Map(position -> Fire)
      val updatedGameBoard = gameBoard.solveEffect(PatternApplication(map))
      updatedGameBoard.board.grid.getToken(position) shouldBe Some(Fire)

    "solve pattern computation correctly" in :
      val updatedGameBoard = gameBoard.solveEffect(PatternComputation(ScratchLine))
      updatedGameBoard.player1.lastPatternChosen should not be List.empty

    "solve card computation correctly" in :
      val updatedGameBoard = gameBoard.solveEffect(CardComputation(12, ScratchLine))
      updatedGameBoard.player1.lastCardChosen should not be List.empty

    "solve wind update correctly" in:
      val updatedGameBoard = gameBoard.solveEffect(UpdateWind(Direction.South))
      updatedGameBoard.board.windDirection shouldBe Direction.South

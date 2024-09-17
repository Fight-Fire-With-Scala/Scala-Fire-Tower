package it.unibo.model.prolog.decisionmaking

import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.{GameBoard, GameBoardConfig, grid}
import it.unibo.model.gameboard.player.{Person, Player, PlayerManager}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.compiletime.uninitialized

class DecisionMakerSpec extends AnyWordSpec with Matchers with PlayerManager:

  var gameBoard: GameBoard = uninitialized

  def initialiseGameBoard(): Unit =
    val initialGameBoard = GameBoard(Person("Player1", List.empty, List.empty), Player.bot(Balanced))
    // Initialize both players
    val (updatedGameBoard, updatedPlayer1) = initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)
    val (finalGameBoard, updatedPlayer2) = initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)
    val completeGameBoard = finalGameBoard
      .copy(player1 = updatedPlayer1, player2 = updatedPlayer2)
    val board = completeGameBoard.board.copy(grid = completeGameBoard.board.grid.setToken(Position(6,7), Fire))
    gameBoard = completeGameBoard.copy(board = board).changePlayer()

  "DecisionMaker" should {

    "compute attack or defense correctly with aggressive behaviour" in {
      initialiseGameBoard()

      val botBehavior = GameBoardConfig.BotBehaviour.Aggressive

      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Attack
      DecisionMaker.getObjectiveTower should contain (Position(0,15))
    }

    "compute attack or defense correctly with balanced behaviour" in {
      initialiseGameBoard()
      // Set bot behavior
      val botBehavior = GameBoardConfig.BotBehaviour.Balanced

      // Call the method to test
      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      // Verify the results
      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Defense
      DecisionMaker.getObjectiveTower should contain(Position(0, 0))
    }

    "compute attack or defense correctly with defensive behaviour" in {
      initialiseGameBoard()
      // Set bot behavior
      val botBehavior = GameBoardConfig.BotBehaviour.Balanced

      // Call the method to test
      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      // Verify the results
      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Defense
      DecisionMaker.getObjectiveTower should contain(Position(0, 0))
    }
  }
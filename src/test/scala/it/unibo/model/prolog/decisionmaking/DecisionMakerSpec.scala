package it.unibo.model.prolog.decisionmaking

import it.unibo.model.GameBoardInitializer
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.{GameBoard, GameBoardConfig, grid}
import it.unibo.model.gameboard.player.{Person, Player, PlayerManager}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.compiletime.uninitialized

class DecisionMakerSpec extends AnyWordSpec with Matchers with GameBoardInitializer with BeforeAndAfterAll:

  var gameBoard: GameBoard = uninitialized

  override def beforeAll(): Unit =
    gameBoard = initialiseGameBoard(Person("Player1", List.empty, List.empty), Player.bot(Balanced))
    val board = gameBoard.board.copy(grid = gameBoard.board.grid.setToken(Position(6, 7), Fire))
    gameBoard = gameBoard.copy(board = board).changePlayer()

  "DecisionMaker" should {

    "compute attack or defense correctly with aggressive behaviour" in {
      val botBehavior = GameBoardConfig.BotBehaviour.Aggressive

      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Attack
      DecisionMaker.getObjectiveTower should contain (Position(0,15))
    }

    "compute attack or defense correctly with balanced behaviour" in {
      val botBehavior = GameBoardConfig.BotBehaviour.Balanced

      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Defense
      DecisionMaker.getObjectiveTower should contain(Position(0, 0))
    }

    "compute attack or defense correctly with defensive behaviour" in {
      val botBehavior = GameBoardConfig.BotBehaviour.Balanced

      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Defense
      DecisionMaker.getObjectiveTower should contain(Position(0, 0))
    }
  }
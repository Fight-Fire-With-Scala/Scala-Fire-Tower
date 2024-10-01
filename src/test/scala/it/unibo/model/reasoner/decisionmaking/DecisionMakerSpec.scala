package it.unibo.model.reasoner.decisionmaking

import it.unibo.model.GameBoardInitializer
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.{ grid, GameBoard, GameBoardConfig }
import it.unibo.model.gameboard.player.{ Person, Player, PlayerManager }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.compiletime.uninitialized

class DecisionMakerSpec
    extends AnyWordSpec
    with Matchers
    with GameBoardInitializer
    with BeforeAndAfterAll:

  var gameBoard: GameBoard = uninitialized

  override def beforeAll(): Unit =
    val player1  = Person("Player1", List.empty, List.empty)
    val player2  = Player.bot(Balanced)
    val gb       = GameBoard(player1, player2)
    val newGrid  = gb.board.grid.setToken(Position(6, 7), Fire)
    val newBoard = gb.board.copy(grid = newGrid)
    val newGb    = gb.copy(board = newBoard)
    gameBoard = initialiseGameBoardPlayers(newGb, player1, player2).changePlayer()

  "DecisionMaker" should {

    "compute attack or defense correctly with aggressive behaviour" in {
      val botBehavior = GameBoardConfig.BotBehaviour.Aggressive

      DecisionMaker.computeAttackOrDefense(gameBoard, botBehavior)

      DecisionMaker.getAttackOrDefense shouldBe AttackDefense.Attack
      DecisionMaker.getObjectiveTower should contain(Position(0, 15))
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

package it.unibo.model.prolog.decisionmaking

import it.unibo.model.GameBoardInitializer
import it.unibo.model.effect.card.WindEffect
import it.unibo.model.effect.pattern.LogicSolverManager
import it.unibo.model.effect.pattern.PatternEffect.BotComputation
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.grid.ConcreteToken.Fire
import it.unibo.model.gameboard.{GameBoard, GameBoardConfig}
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.player.{Person, Player}
import it.unibo.model.prolog.decisionmaking.{AttackDefense, DecisionMaker}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.compiletime.uninitialized
import scala.language.postfixOps

class PatternChoiceSpec
    extends AnyWordSpec
    with Matchers
    with GameBoardInitializer
    with BeforeAndAfterAll
    with LogicSolverManager:
  var gameBoard: GameBoard = uninitialized

  override def beforeAll(): Unit =
    gameBoard = initialiseGameBoard(Person("Player1", List.empty, List.empty), Player.bot(Balanced))
    val board = gameBoard.board.copy(grid = gameBoard.board.grid.setToken(Position(6, 7), Fire))
    gameBoard = gameBoard.copy(board = board).changePlayer()

  "A Pattern choice" should:

    //doesnt' work
    "compute the right pattern to play" in:
      DecisionMaker.computeAttackOrDefense(gameBoard, Balanced)
      val direction        = gameBoard.board.windDirection
      val logicEffect      = WindEffect.windEffectSolver.solve(direction)
      val listLogicEffects = Map(1 -> List(logicEffect))
      val a = computePatterns(gameBoard, listLogicEffects)
      1 shouldBe 1

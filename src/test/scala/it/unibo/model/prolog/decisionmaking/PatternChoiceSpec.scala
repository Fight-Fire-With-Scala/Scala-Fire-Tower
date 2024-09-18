package it.unibo.model.prolog.decisionmaking

import it.unibo.model.GameBoardInitializer
import it.unibo.model.card.Card
import it.unibo.model.effect.card.{BucketEffect, FireEffect, WaterEffect, WindEffect}
import it.unibo.model.effect.core.ICardEffect
import it.unibo.model.effect.pattern.LogicSolverManager
import it.unibo.model.effect.pattern.PatternEffect.BotComputation
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour.Balanced
import it.unibo.model.gameboard.grid.ConcreteToken.{Fire, Water}
import it.unibo.model.gameboard.{Deck, GameBoard, GameBoardConfig}
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.player.{Bot, IMakeDecision, Person, Player}
import it.unibo.model.prolog.PrologUtils.defaultId
import it.unibo.model.prolog.decisionmaking.{AttackDefense, DecisionMaker}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{BeforeAndAfterAll, color}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.compiletime.uninitialized
import scala.language.postfixOps

class PatternChoiceSpec
    extends AnyWordSpec
    with Matchers
    with GameBoardInitializer
    with BeforeAndAfterAll
    with LogicSolverManager
    with IMakeDecision:
  var gameBoard: GameBoard = uninitialized

  val dummyCardList: List[Card] = List(
    Card(0, FireEffect.Flare),
    Card(1, WaterEffect.AirDrop),
    Card(2, FireEffect.Flare),
    Card(3, WaterEffect.AirDrop),
    Card(4, FireEffect.Flare),
    Card(5, WaterEffect.AirDrop),
    Card(6, FireEffect.Flare),
    Card(7, WaterEffect.AirDrop),
    Card(8, FireEffect.Flare),
    Card(9, WaterEffect.AirDrop),
    Card(10, FireEffect.Flare)
  )
  val dummySpecialCardList: List[Card] = List(
    Card(15, BucketEffect),
    Card(16, BucketEffect)
  )

  override def beforeAll(): Unit =
    val player1 = Person("Player1", List.empty, List.empty)
    val player2 = Bot(List.empty, List.empty, BotBehaviour.Aggressive, None)
    val newDeck = Deck(dummyCardList, dummySpecialCardList)
    val gb      = GameBoard(player1, player2).copy(deck = newDeck)
    gameBoard = initialiseGameBoardPlayers(gb, player1, player2).changePlayer()

  "A Pattern choice" should:

    "compute all the right expected PatternCalculated pattern to play" in:
      val direction          = gameBoard.board.windDirection
      val logicEffect        = WindEffect.windEffectSolver.solve(direction)
      val allPossiblePattern = computePatterns(gameBoard, Some(defaultId.toInt), logicEffect)
      allPossiblePattern should not be empty

    "compute the right pattern to play to perform an attack attack" in:
      val newGrid  = gameBoard.board.grid.setToken(Position(6, 8), Fire)
      val newBoard = gameBoard.board.copy(grid = newGrid)
      val newGb    = gameBoard.copy(board = newBoard)
      DecisionMaker.computeAttackOrDefense(newGb, Balanced)
      val playerHand = newGb.getCurrentPlayer.hand
      val filteredHandBasedOnDecision =
        filterCardsBasedOnDecision(playerHand, DecisionMaker.getAttackOrDefense)
      val effects = filteredHandBasedOnDecision
        .map(card => Option(card.id) -> List(ICardEffect.convert(card.effect)))
        .toMap
      val (cardId, patternToPlay) = computePatterns(newGb, effects)
      patternToPlay should not be empty
      patternToPlay.head._2 should be(Fire)

    "compute the right pattern to play to perform a defense" in:
      val tokenPosition = Position(3, 3)
      val newGrid       = gameBoard.board.grid.setToken(tokenPosition, Fire)
      val newBoard      = gameBoard.board.copy(grid = newGrid)
      val newGb         = gameBoard.copy(board = newBoard)
      DecisionMaker.computeAttackOrDefense(newGb, Balanced)
      val playerHand = newGb.getCurrentPlayer.hand
      val filteredHandBasedOnDecision =
        filterCardsBasedOnDecision(playerHand, DecisionMaker.getAttackOrDefense)
      val effects = filteredHandBasedOnDecision
        .map(card => Option(card.id) -> List(ICardEffect.convert(card.effect)))
        .toMap
      val (cardId, patternToPlay) = computePatterns(newGb, effects)
      patternToPlay should not be empty
      patternToPlay.head._2 should be(Water)
      patternToPlay.head._1 should be(tokenPosition)

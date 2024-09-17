package it.unibo.model.gameboard.player

import it.unibo.launcher.Launcher.ModelImpl
import it.unibo.model.GameBoardInitializer
import it.unibo.model.ModelModule.Model
import it.unibo.model.card.Card
import it.unibo.model.effect.card.{ BucketEffect, FireEffect, WaterEffect }
import it.unibo.model.gameboard.{ Deck, GameBoard, GamePhase }
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.ConcreteToken.{ Fire, Firebreak }
import it.unibo.model.gameboard.grid.{ ConcreteToken, Position }
import it.unibo.model.prolog.decisionmaking.DecisionMaker
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterAll

import scala.compiletime.uninitialized

class BotThinkSpec
    extends AnyWordSpec
    with Matchers
    with GameBoardInitializer
    with BeforeAndAfterAll:

  var gameBoard: GameBoard = uninitialized

  val dummyCardList: List[Card] = List(
    Card(0, FireEffect.Explosion),
    Card(1, FireEffect.Flare),
    Card(2, WaterEffect.AirDrop),
    Card(3, WaterEffect.FireEngine),
    Card(4, FireEffect.BurningSnag),
    Card(5, FireEffect.Explosion),
    Card(6, FireEffect.Flare),
    Card(7, FireEffect.Flare),
    Card(8, FireEffect.Flare),
    Card(9, FireEffect.BurningSnag),
    Card(10, FireEffect.Explosion),
    Card(11, FireEffect.Flare),
    Card(12, FireEffect.Flare),
    Card(13, FireEffect.Flare),
    Card(14, FireEffect.BurningSnag)
  )
  val dummySpecialCardList: List[Card] = List(
    Card(15, BucketEffect),
    Card(16, BucketEffect)
  )

  override def beforeAll(): Unit =
    val player1 = Bot(List.empty, List.empty, BotBehaviour.Defensive, None)
    val player2 = Bot(List.empty, List.empty, BotBehaviour.Aggressive, None)
    val newDeck = Deck(dummyCardList, dummySpecialCardList)
    val gb      = GameBoard(player1, player2).copy(deck = newDeck)
    gameBoard = initialiseGameBoardPlayers(gb, player1, player2)

  private def createModelWithGameBoard(phase: GamePhase): Model =
    val model        = new ModelImpl()
    val gameBoardNew = gameBoard.copy(gamePhase = phase)
    model.setGameBoard(gameBoardNew)
    model

  "A Bot" should:
    "think for the WindPhase and update the game board" in:
      val model      = createModelWithGameBoard(WindPhase)
      val bot        = model.getGameBoard.getCurrentPlayer.asInstanceOf[Bot]
      given m: Model = model
      val prevBoard  = m.getGameBoard.board
      bot.think
      m.getGameBoard.board should not be prevBoard

    "discard and redraw his hand in RedrawCardPhase" in:
      val model = createModelWithGameBoard(RedrawCardsPhase)
      val bot   = model.getGameBoard.getCurrentPlayer.asInstanceOf[Bot]

      given m: Model = model

      val prevHand = m.getGameBoard.getCurrentPlayer.hand
      bot.think
      m.getGameBoard.getCurrentPlayer.hand should not be prevHand

    "think for the PlayStandardCardPhase and update the game board" in:
      val model      = createModelWithGameBoard(PlayStandardCardPhase)
      val bot        = model.getGameBoard.getCurrentPlayer.asInstanceOf[Bot]
      given m: Model = model
      DecisionMaker.setObjectiveTower(m.getGameBoard.getOpponent.towerPositions.map(_.position))
      val prevGb = m.getGameBoard
      val boardWithTokens = prevGb.board.copy(grid =
        gameBoard.board.grid.setTokens((Position(6, 7), Fire), (Position(6, 8), Firebreak))
      )
      val prevHand = bot.hand
      val newGb    = prevGb.copy(board = boardWithTokens)
      m.setGameBoard(newGb)
      println(bot.hand)
      bot.think

      m.getGameBoard.getCurrentPlayer.hand should not be prevHand
      m.getGameBoard.board should not be newGb

    "think for the PlaySpecialCardPhase and update the game board" in:
      val model = createModelWithGameBoard(PlaySpecialCardPhase)
      val bot   = model.getGameBoard.getCurrentPlayer.asInstanceOf[Bot]

      given m: Model = model

      DecisionMaker.setObjectiveTower(m.getGameBoard.getOpponent.towerPositions.map(_.position))

      val prevGb = m.getGameBoard
      val boardWithTokens =
        prevGb.board.copy(grid = gameBoard.board.grid.setToken(Position(1, 14), Fire))

      val newGb = prevGb.copy(board = boardWithTokens)
      m.setGameBoard(newGb)
      bot.think
      m.getGameBoard.getCurrentPlayer.extraCard.isDefined shouldBe false
      m.getGameBoard.board should not be newGb

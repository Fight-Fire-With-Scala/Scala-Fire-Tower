package it.unibo.model

import monix.reactive.subjects.PublishSubject
import it.unibo.controller.{ModelMessage, ModelSubject, RefreshMessage, StartGameBoardMessage}
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.players.Player
import it.unibo.model.settings.Settings
import scala.compiletime.uninitialized

object ModelModule:
  trait Model:
    def initialiseModel(settings: Settings): Unit
    def getObservable: ModelSubject
    def getGameBoard: GameBoard
    def setGameBoard(newGameBoard: GameBoard): Unit
    def drawCards(nCards: Int, player: Option[Player] = None): Unit
    def discardCards(cards: List[Int]): Unit
    def fillPlayerHand(): Unit

  trait Provider:
    val model: Model

  trait Component:
    class ModelImpl extends Model:
      private var gameBoard: GameBoard = uninitialized
      private val observerSubject = PublishSubject[ModelMessage]()

      def getObservable: ModelSubject = observerSubject
      override def getGameBoard: GameBoard = gameBoard
      override def setGameBoard(newGameBoard: GameBoard): Unit =
        gameBoard = newGameBoard
        observerSubject.onNext(RefreshMessage(gameBoard))

      def initialiseModel(settings: Settings): Unit =
        val playerOne = settings.getPlayerOne
        val playerTwo = settings.getPlayerTwo
        gameBoard = GameBoard(playerOne, playerTwo, observerSubject)
        fillPlayerHand()
        observerSubject.onNext(StartGameBoardMessage(gameBoard))

      def drawCards(nCards: Int, player: Option[Player] = None): Unit =
        val currentPlayer = player.getOrElse(gameBoard.currentPlayer)
        val deck = gameBoard.deck
        val (finalDeck, finalPlayer) = (1 to nCards).foldLeft((deck, currentPlayer)) {
          case ((currentDeck, currentPlayer), _) =>
            val (card, newDeck) = currentDeck.drawCard()
            val newPlayer = currentPlayer.drawCardFromDeck(card)
            (newDeck, newPlayer)
        }
        setGameBoard(gameBoard.copy(deck = finalDeck, currentPlayer = finalPlayer))

      def discardCards(cards: List[Int]): Unit =
        val player = gameBoard.currentPlayer
        setGameBoard(gameBoard.copy(currentPlayer = player.discardCards(cards)))

      def fillPlayerHand(): Unit =
        val cardToDraw = 5 - gameBoard.currentPlayer.hand.size
        drawCards(cardToDraw)

  trait Interface extends Provider with Component

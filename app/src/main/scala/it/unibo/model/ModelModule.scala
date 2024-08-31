package it.unibo.model

import monix.reactive.subjects.PublishSubject
import it.unibo.controller.{
  ConfirmCardPlayMessage,
  ModelMessage,
  ModelSubject,
  RefreshMessage,
  ShowAvailablePatterns,
  StartGameBoardMessage
}
import it.unibo.model.cards.choices.WindChoice
import it.unibo.model.cards.effects.PatternChoiceEffect
import it.unibo.model.cards.types.{FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.grid.{Position, Token}
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
    def confirmCardPlay(): Unit
    def setCurrentCardId(cardId: Int): Unit
    def resolvePatternComputation(cardId: Int): Unit
    def resolvePatternChoice(pattern: Map[Position, Token]): Unit

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

      override def confirmCardPlay(): Unit = observerSubject.onNext(ConfirmCardPlayMessage())

      override def setCurrentCardId(cardId: Int): Unit =
        setGameBoard(gameBoard.copy(board = gameBoard.board.copy(currentCardId = Some(cardId))))

      override def resolvePatternComputation(cardId: Int): Unit =
        val card = gameBoard.currentPlayer.hand.find(_.id == cardId)
        card match
          case Some(c) => c.cardType.effectType match
              case card: FireCard      => ???
              case card: FirebreakCard => ???
              case card: WaterCard     => ???
              case card: WindCard      =>
                val gb = gameBoard.resolveCardPlayed(c, WindChoice.PlaceFire)
                observerSubject.onNext(ShowAvailablePatterns(gb.board.availablePatterns))
              case _                   => ???
          case None    => logger.warn("No card found")

      override def resolvePatternChoice(pattern: Map[Position, Token]): Unit =
        val effect = PatternChoiceEffect(pattern)
        val board = gameBoard.board.applyEffect(Some(effect))
        val currentPlayer = gameBoard.currentPlayer
        setGameBoard(gameBoard.copy(
          currentPlayer = board.currentCardId match
            case Some(cardId) => currentPlayer.playCard(cardId)._1
            case None         => currentPlayer
          ,
          board = board.copy(currentCardId = None)
        ))
        confirmCardPlay()
        observerSubject.onNext(ShowAvailablePatterns(List.empty))
  trait Interface extends Provider with Component

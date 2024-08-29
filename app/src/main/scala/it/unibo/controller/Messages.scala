package it.unibo.controller

import it.unibo.model.gameboard.{Direction, GameBoard, GamePhase}
import it.unibo.model.gameboard.grid.{Position, Token}
import it.unibo.model.settings.Settings
import monix.reactive.subjects.PublishSubject

type ModelSubject = PublishSubject[ModelMessage]
type ViewSubject = PublishSubject[ViewMessage]
type InternalViewSubject = PublishSubject[InternalViewMessage]

sealed trait Message

/*
 * This refers to messages sent to the model from the view
 */
sealed trait ViewMessage extends Message
case class SettingsMessage(settings: Settings) extends ViewMessage
case class DrawCardMessage(nCards: Int) extends ViewMessage
case class SetupWindPhase() extends ViewMessage
case class UpdateWindDirection(windDirection: Direction) extends ViewMessage
case class EndWindPhase() extends ViewMessage
case class ResolvePatternComputation(cardId: Int) extends ViewMessage
case class ResolvePatternChoice(pattern: Map[Position, Token]) extends ViewMessage
case class DiscardTheseCardsMessage(cards: List[Int]) extends ViewMessage

/*
 * This refers to messages sent to the view from the model
 */
sealed trait ModelMessage extends Message
case class StartGameBoardMessage(gameBoard: GameBoard) extends ModelMessage
case class ShowAvailablePatterns(p: List[Map[Position, Token]]) extends ModelMessage
case class ChangeTurnPhase(gamePhase: GamePhase) extends ModelMessage
case class RefreshMessage(gameBoard: GameBoard) extends ModelMessage

// potrebbe estendere in una qualche maniera ResolvePatternChoice ?
// al massimo la risoluzione di resolvepatternchoice è un metodo privato che viene richiamato  da entrambe
// chiami lo stesso metodo al limite 
// sì esatto lo facciamo come metodo in comune

/*
 * This refers to messages sent to the view from the view
 */
sealed trait InternalViewMessage extends Message

case class SetupActionPhase() extends InternalViewMessage
case class InitializeDiscardProcedureMessage() extends InternalViewMessage
case class ToggleCardInListMessage(cardId: Int) extends InternalViewMessage
case class ConfirmDiscardMessage() extends InternalViewMessage
case class CancelDiscardMessage() extends InternalViewMessage
case class CandidateCardToPlayMessage(cardId: Int) extends InternalViewMessage

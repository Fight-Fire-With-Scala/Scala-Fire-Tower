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

case class GameBoardInitialization(settings: Settings) extends ViewMessage

case class UpdateWindDirection(windDirection: Direction) extends ViewMessage
case class UpdateGamePhaseModel(gamePhase: GamePhase) extends ViewMessage

case class ResolvePatternComputation(cardId: Int) extends ViewMessage
case class ResetPatternComputation() extends ViewMessage
case class ResolvePatternChoice(pattern: Map[Position, Token]) extends ViewMessage

case class DrawCardMessage(nCards: Int) extends ViewMessage
case class DiscardCardMessage(cards: List[Int]) extends ViewMessage

/*
 * This refers to messages sent to the view from the model
 */
sealed trait ModelMessage extends Message

case class StartGameBoardMessage(gameBoard: GameBoard) extends ModelMessage
case class RefreshMessage(gameBoard: GameBoard) extends ModelMessage
case class ConfirmCardPlayMessage() extends ModelMessage

/*
 * This refers to messages sent to the view from the view
 */
sealed trait InternalViewMessage extends Message

case class UpdateGamePhaseView(gamePhase: GamePhase) extends InternalViewMessage
case class InitializeDiscardProcedureMessage() extends InternalViewMessage
case class ToggleCardInListMessage(cardId: Int) extends InternalViewMessage
case class ConfirmDiscardMessage() extends InternalViewMessage
case class CancelDiscardMessage() extends InternalViewMessage
case class CandidateCardToPlayMessage(cardId: Int) extends InternalViewMessage

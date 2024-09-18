package it.unibo.controller

import it.unibo.controller.view.RefreshType
import it.unibo.model.effect.card.WindUpdateEffect
import it.unibo.model.effect.hand.HandEffect.DiscardCard
import it.unibo.model.effect.hand.HandEffect.DrawCard
import it.unibo.model.effect.hand.HandEffect.PlayCard
import it.unibo.model.effect.pattern.PatternEffect.PatternApplication
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GameBoardConfig
import monix.reactive.subjects.PublishSubject

type ModelSubject        = PublishSubject[ModelMessage]
type ViewSubject         = PublishSubject[ViewMessage]
type BotSubject          = PublishSubject[BotMessage]
type InternalViewSubject = PublishSubject[InternalViewMessage]

sealed trait Message

sealed trait BotMessage extends Message

/*
 * This refers to messages sent to the model from the view
 */
sealed trait ViewMessage extends Message

final case class GameBoardInitializationMessage(settings: GameBoardConfig) extends ViewMessage

final case class UpdateWindDirectionMessage(ef: WindUpdateEffect) extends ViewMessage
final case class UpdateGamePhaseMessage(ef: PhaseEffect) extends ViewMessage with BotMessage

final case class ChoseCardToPlayMessage(ef: PlayCard) extends ViewMessage
final case class ResolveCardResetMessage() extends ViewMessage
final case class ResolvePatternChoiceMessage(ef: PatternApplication) extends ViewMessage

final case class DrawCardMessage(ef: DrawCard) extends ViewMessage
final case class DiscardCardMessage(ef: DiscardCard) extends ViewMessage

/*
 * This refers to messages sent to the view from the model
 */
sealed trait ModelMessage extends Message

final case class StartGameMessage(gb: GameBoard) extends ModelMessage
final case class RefreshMessage(gb: GameBoard, refreshType: RefreshType) extends ModelMessage
final case class ConfirmCardPlayMessage() extends ModelMessage

/*
 * This refers to messages sent to the view from the view
 */
sealed trait InternalViewMessage extends Message

final case class InitializeDiscardProcedureMessage() extends InternalViewMessage
final case class ToggleCardInListMessage(cardId: Int) extends InternalViewMessage
final case class ConfirmDiscardMessage() extends InternalViewMessage
final case class CancelDiscardMessage() extends InternalViewMessage
final case class CandidateCardToPlayMessage(cardId: Int) extends InternalViewMessage

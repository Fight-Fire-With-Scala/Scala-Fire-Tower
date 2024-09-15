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

case class GameBoardInitialization(settings: GameBoardConfig) extends ViewMessage

case class UpdateWindDirection(ef: WindUpdateEffect) extends ViewMessage
case class UpdateGamePhase(ef: PhaseEffect) extends ViewMessage with BotMessage

case class ChoseCardToPlay(ef: PlayCard) extends ViewMessage
case class ResolveCardReset() extends ViewMessage
case class ResolvePatternChoice(ef: PatternApplication) extends ViewMessage

case class DrawCardMessage(ef: DrawCard) extends ViewMessage
case class DiscardCardMessage(ef: DiscardCard) extends ViewMessage

/*
 * This refers to messages sent to the view from the model
 */
sealed trait ModelMessage extends Message

case class StartGameMessage(gb: GameBoard) extends ModelMessage
case class RefreshMessage(gb: GameBoard, refreshType: RefreshType) extends ModelMessage
case class ConfirmCardPlayMessage() extends ModelMessage

/*
 * This refers to messages sent to the view from the view
 */
sealed trait InternalViewMessage extends Message

case class InitializeDiscardProcedureMessage() extends InternalViewMessage
case class ToggleCardInListMessage(cardId: Int) extends InternalViewMessage
case class ConfirmDiscardMessage() extends InternalViewMessage
case class CancelDiscardMessage() extends InternalViewMessage
case class CandidateCardToPlayMessage(cardId: Int) extends InternalViewMessage

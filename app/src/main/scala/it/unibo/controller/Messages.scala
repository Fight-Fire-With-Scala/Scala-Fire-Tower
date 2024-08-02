package it.unibo.controller

import it.unibo.model.gameboard.GameBoard
import it.unibo.model.settings.Settings
import monix.reactive.subjects.PublishSubject

type ModelSubject = PublishSubject[ModelMessage]
type ViewSubject = PublishSubject[ViewMessage]

sealed trait Message

/*
 * This refers to messages sent to the model from the view
 */
sealed trait ViewMessage extends Message
case class SettingsMessage(settings: Settings) extends ViewMessage

/*
 * This refers to messages sent to the view from the model
 */
sealed trait ModelMessage extends Message
case class StartGameBoardMessage(gameBoard: GameBoard) extends ModelMessage

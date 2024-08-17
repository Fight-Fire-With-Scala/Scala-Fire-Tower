package it.unibo.controller

import it.unibo.model.gameboard.{Direction, GameBoard}
import it.unibo.model.gameboard.grid.{Position, Token}
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
case class DrawCardMessage(nCards: Int) extends ViewMessage
case class ResolveWindPhase() extends ViewMessage
case class UpdateWindDirection(windDirection: Direction) extends ViewMessage
/*
 * This refers to messages sent to the view from the model
 */
sealed trait ModelMessage extends Message
case class StartGameBoardMessage(gameBoard: GameBoard) extends ModelMessage
case class ShowAvailablePatterns(p: List[Map[Position, Token]]) extends ModelMessage
case class RefreshMessage(gameBoard: GameBoard) extends ModelMessage

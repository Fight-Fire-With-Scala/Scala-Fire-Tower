package it.unibo.model.gameboard.player

import it.unibo.controller.model.ModelController
import it.unibo.model.card.Card
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.TowerPosition
import it.unibo.model.{gameboard, logger}

final case class Bot(
    moves: List[Move],
    hand: List[Card],
    botBehaviour: BotBehaviour,
    towerPositions: Set[TowerPosition],
    extraCard: Option[Card] = None
) extends ThinkingPlayer:
  override val name: String = "BOT"

  override def updatePlayer(moves: List[Move], hand: List[Card], extraCard: Option[Card]): Player =
    copy(moves = moves, hand = hand, extraCard = extraCard)

  override def think(controller: ModelController): Unit =
    logger.info("[BOT] Starting to think")
    given c: ModelController = controller

    controller.model.getGameBoard.gamePhase match
      case WindPhase             => thinkForWindPhase
      case WaitingPhase          => thinkForWaitingPhase
      case RedrawCardsPhase      => thinkForRedrawCardPhase
      case PlayStandardCardPhase => thinkForPlayStandardCardPhase
      case DecisionPhase         => thinkForDecisionPhase
      case PlaySpecialCardPhase  => thinkForPlaySpecialCardPhase
      case EndTurnPhase          => thinkForEndTurnPhase

object Bot:
  def apply(moves: List[Move], hand: List[Card], botBehaviour: BotBehaviour) =
    new Bot(List.empty, List.empty, botBehaviour, Set.empty)

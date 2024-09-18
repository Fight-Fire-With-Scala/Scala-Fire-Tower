package it.unibo.model.gameboard.player

import it.unibo.controller.BotSubject
import it.unibo.model.ModelModule.Model
import it.unibo.model.card.Card
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.*
import it.unibo.model.gameboard.grid.TowerPosition
import it.unibo.model.logger
import it.unibo.controller.UpdateGamePhaseMessage

final case class Bot(
    moves: List[Move],
    hand: List[Card],
    botBehaviour: BotBehaviour,
    towerPositions: Set[TowerPosition],
    botObservable: Option[BotSubject],
    extraCard: Option[Card] = None
) extends ThinkingPlayer:
  override val name: String = "BOT"

  override def updatePlayer(moves: List[Move], hand: List[Card], extraCard: Option[Card]): Player =
    copy(moves = moves, hand = hand, extraCard = extraCard)

  override def onUpdateGamePhaseRequest(model: Model, phaseEffect: PhaseEffect): Unit =
    botObservable match
      case Some(observable) =>
        model.getGameBoard.isGameEnded match
          case Some(_) => observable.onNext(UpdateGamePhaseMessage(PhaseEffect(EndGamePhase)))
          case None    => observable.onNext(UpdateGamePhaseMessage(phaseEffect))
      case None =>

  override def think(using model: Model): Unit =
    logger.info("[BOT] Starting to think")

    model.getGameBoard.gamePhase match
      case WindPhase             => thinkForWindPhase
      case WaitingPhase          => thinkForWaitingPhase
      case RedrawCardsPhase      => thinkForRedrawCardPhase
      case PlayStandardCardPhase => thinkForPlayStandardCardPhase
      case DecisionPhase         => thinkForDecisionPhase
      case PlaySpecialCardPhase  => thinkForPlaySpecialCardPhase
      case _                     =>

object Bot:
  def apply(
      moves: List[Move],
      hand: List[Card],
      botBehaviour: BotBehaviour,
      botObservable: Option[BotSubject]
  ) = new Bot(List.empty, List.empty, botBehaviour, Set.empty, botObservable)

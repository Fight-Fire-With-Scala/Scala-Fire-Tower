package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.{ ChoseCardToPlayMessage, ConfirmCardPlayMessage, DiscardCardMessage, DrawCardMessage, GameBoardInitializationMessage, RefreshMessage, ResolveCardResetMessage, ResolvePatternChoiceMessage, StartGameMessage, UpdateGamePhaseMessage, UpdateWindDirectionMessage, ViewMessage }
import it.unibo.controller.model.ModelController
import it.unibo.controller.view.RefreshType
import it.unibo.controller.view.RefreshType.{ CardDeselected, CardDiscard, CardDraw, CardSelected, EndGameUpdate, PatternChosen, PhaseUpdate, WindUpdate }
import it.unibo.model.effect.card.WindUpdateEffect
import it.unibo.model.effect.hand.HandEffect
import it.unibo.model.effect.hand.HandEffect.{ DiscardCard, DrawCard, PlayCard }
import it.unibo.model.effect.pattern.PatternEffect
import it.unibo.model.effect.pattern.PatternEffect.{ PatternApplication, ResetPatternComputation }
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoardConfig
import it.unibo.model.gameboard.GamePhase.{ DecisionPhase, EndGamePhase, PlaySpecialCardPhase, PlayStandardCardPhase, WaitingPhase, WindPhase }

/** This class is subscribed to the View updates and changes the Model accordingly */
final class ViewSubscriber(val controller: ModelController)
    extends BaseSubscriber[ViewMessage]
    with UpdateGamePhaseHandler:

  override val logger: Logger = Logger("View -> ViewSubscriber")

  override def onMessageReceived(msg: ViewMessage): Unit = msg match
    case UpdateGamePhaseMessage(ef: PhaseEffect) =>
      handleUpdateGamePhase(ef)

    case UpdateWindDirectionMessage(ef: WindUpdateEffect) =>
      controller.applyEffect(ef, WindUpdate)
      controller.modelObserver.onNext(ConfirmCardPlayMessage())

    case ChoseCardToPlayMessage(ef: PlayCard) => controller.applyEffect(ef, CardSelected)

    case ResolvePatternChoiceMessage(ef: PatternApplication) =>
      controller.applyEffect(ef, RefreshType.PatternChosen)
      controller.modelObserver.onNext(RefreshMessage(controller.model.getGameBoard, CardDeselected))
      controller.applyEffect(ResetPatternComputation, CardDeselected)
      controller.modelObserver.onNext(ConfirmCardPlayMessage())
      controller.model.getGameBoard.isGameEnded match
        case Some(_) =>
          controller.applyEffect(PhaseEffect(EndGamePhase), PhaseUpdate)
          controller.modelObserver.onNext(
            RefreshMessage(controller.model.getGameBoard, EndGameUpdate)
          )
        case None =>
          controller.model.getGameBoard.gamePhase match
            case WindPhase =>
              controller.applyEffect(PhaseEffect(WaitingPhase), PhaseUpdate)
            case PlayStandardCardPhase | PlaySpecialCardPhase =>
              controller.applyEffect(PhaseEffect(DecisionPhase), PhaseUpdate)
            case _ =>

    case ResolveCardResetMessage() =>
      controller.applyEffect(ResetPatternComputation, CardDeselected)

    case DrawCardMessage(ef: DrawCard) => controller.applyEffect(ef, CardDraw)

    case DiscardCardMessage(ef: DiscardCard) => controller.applyEffect(ef, CardDiscard)

    case GameBoardInitializationMessage(settings) =>
      val initialGameBoard = controller.initializeGameBoard(settings)
      controller.model.setGameBoard(initialGameBoard)
      controller.modelObserver.onNext(StartGameMessage(initialGameBoard))

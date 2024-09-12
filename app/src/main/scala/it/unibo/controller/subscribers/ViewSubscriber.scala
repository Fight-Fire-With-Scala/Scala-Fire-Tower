package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.{
  ChoseCardToPlay,
  ConfirmCardPlayMessage,
  DiscardCardMessage,
  DrawCardMessage,
  GameBoardInitialization,
  RefreshType,
  ResolvePatternChoice,
  ResolvePatternReset,
  StartGameMessage,
  UpdateGamePhase,
  UpdateWindDirection,
  ViewMessage
}
import it.unibo.controller.RefreshType.{
  CardDeselected,
  CardDiscard,
  CardDraw,
  CardSelected,
  PatternChosen,
  PhaseUpdate,
  WindUpdate
}
import it.unibo.model.ModelModule.Model
import it.unibo.model.effects.PatternEffect
import it.unibo.model.effects.hand.HandEffect.{DiscardCard, DrawCard, PlayCard}
import it.unibo.model.effects.PatternEffect.{PatternApplication, ResetPatternComputation}
import it.unibo.model.effects.cards.WindChoiceEffect
import it.unibo.model.effects.hand.HandEffect
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.controller.model.ModelController

/** This class is subscribed to the View updates and changes the Model accordingly */
final class ViewSubscriber(controller: ModelController) extends BaseSubscriber[ViewMessage]:

  given Conversion[Model, GameBoard] = _.getGameBoard

  override val logger: Logger = Logger("View -> ViewSubscriber")

  override def onMessageReceived(msg: ViewMessage): Unit = msg match
    case GameBoardInitialization(settings) =>
      val initialGameBoard = GameBoard(settings.getPlayerOne, settings.getPlayerTwo)

      // Initialize both players
      val (updatedGameBoard, updatedPlayer1) = controller
        .initializePlayer(initialGameBoard, initialGameBoard.getCurrentPlayer)
      val (finalGameBoard, updatedPlayer2) = controller
        .initializePlayer(updatedGameBoard, updatedGameBoard.getOpponent)

      // Create the fully initialized game board
      val completeGameBoard = finalGameBoard
        .copy(player1 = updatedPlayer1, player2 = updatedPlayer2)

      // Update the game board in the model and notify observers
      val resolvedGameBoard = completeGameBoard
        .resolveEffect(PhaseEffect(completeGameBoard.gamePhase))
      controller.model.setGameBoard(resolvedGameBoard)
      controller.modelObserver.onNext(StartGameMessage(resolvedGameBoard))

    case UpdateGamePhase(ef: PhaseEffect) =>
      controller.applyEffect(ef, PhaseUpdate)
      controller.activateBot()

    case UpdateWindDirection(ef: WindChoiceEffect) => controller.applyEffect(ef, WindUpdate)

    case ChoseCardToPlay(ef: PlayCard) => controller.applyEffect(ef, CardSelected)

    case ResolvePatternChoice(ef: PatternApplication) =>
      controller.applyEffect(ef, RefreshType.PatternChosen)
      controller.applyEffect(ResetPatternComputation, CardDeselected)
      controller.modelObserver.onNext(ConfirmCardPlayMessage())

    case ResolvePatternReset() => controller.applyEffect(ResetPatternComputation, CardDeselected)

    case DrawCardMessage(ef: DrawCard) => controller.applyEffect(ef, CardDraw)

    case DiscardCardMessage(ef: DiscardCard) => controller.applyEffect(ef, CardDiscard)

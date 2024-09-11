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
  UpdateGamePhaseModel,
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
      val playerOne = settings.getPlayerOne
      val playerTwo = settings.getPlayerTwo
      val gameBoard = GameBoard(playerOne, playerTwo)
      val (partialGb, newPlayer) = controller
        .initializePlayer(gameBoard, gameBoard.getCurrentPlayer)
      val (newGb, newPlayer2) = controller.initializePlayer(partialGb, partialGb.getOpponent)
      controller.model.setGameBoard(newGb.copy(player1 = newPlayer, player2 = newPlayer2))
      controller.modelObserver.onNext(StartGameMessage(newGb))

    case UpdateGamePhaseModel(ef: PhaseEffect) =>
      controller.applyEffect(ef, PhaseUpdate)
      controller.activateBot()

    case UpdateWindDirection(ef: WindChoiceEffect) => controller.applyEffect(ef, WindUpdate)

    case ChoseCardToPlay(ef: PlayCard) => controller.applyEffect(ef, CardSelected)

    case ResolvePatternChoice(ef: PatternApplication) =>
      controller.applyEffect(ef, RefreshType.PatternChosen)
      controller.modelObserver.onNext(ConfirmCardPlayMessage())

    case ResolvePatternReset() => controller.applyEffect(ResetPatternComputation, CardDeselected)

    case DrawCardMessage(ef: DrawCard) => controller.applyEffect(ef, CardDraw)

    case DiscardCardMessage(ef: DiscardCard) => controller.applyEffect(ef, CardDiscard)

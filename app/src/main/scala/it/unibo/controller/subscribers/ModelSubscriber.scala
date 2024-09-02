package it.unibo.controller.subscribers

import it.unibo.view.ViewModule.View
import com.typesafe.scalalogging.Logger
import it.unibo.controller.{
  ConfirmCardPlayMessage,
  ModelMessage,
  RefreshMessage,
  StartGameBoardMessage
}

/** This class is subscribed to the Model updates and changes the View accordingly */
final class ModelSubscriber(view: View) extends BaseSubscriber[ModelMessage]:

  override val logger: Logger = Logger("Model -> ModelSubscriber")

  override def onMessageReceived(msg: ModelMessage): Unit = msg match
    case StartGameBoardMessage(gameBoard) => view.startGame(gameBoard)
    case RefreshMessage(gameBoard)        => view.updateView(gameBoard)
    case ConfirmCardPlayMessage()         => view.confirmCardPlay()

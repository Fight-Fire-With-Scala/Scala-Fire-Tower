package it.unibo.controller.subscribers

import it.unibo.view.ViewModule.View
import com.typesafe.scalalogging.Logger
import it.unibo.controller.{
  ConfirmCardPlayMessage,
  ModelMessage,
  RefreshMessage,
  StartGameMessage,
  StartMenuMessage
}

/** This class is subscribed to the Model updates and changes the View accordingly */
final class ModelSubscriber(view: View) extends BaseSubscriber[ModelMessage]:

  override val logger: Logger = Logger("Model -> ModelSubscriber")

  override def onMessageReceived(msg: ModelMessage): Unit = msg match
    case StartGameMessage(gameBoard)            => view.startGame(gameBoard)
    case RefreshMessage(gameBoard, refreshType) => view.updateView(gameBoard, refreshType)
    case ConfirmCardPlayMessage()               => view.confirmCardPlay()
    case StartMenuMessage()                     => ???

package it.unibo.controller.subscribers

import com.typesafe.scalalogging.Logger
import it.unibo.controller.view.ViewController
import it.unibo.controller.{
  ConfirmCardPlayMessage,
  ModelMessage,
  RefreshMessage,
  StartGameMessage,
  StartMenuMessage
}

/** This class is subscribed to the Model updates and changes the View accordingly */
final class ModelSubscriber(controller: ViewController)
    extends BaseSubscriber[ModelMessage]:
  
  override val logger: Logger = Logger("Model -> ModelSubscriber")

  override def onMessageReceived(msg: ModelMessage): Unit = msg match
    case StartGameMessage(gameBoard)            => controller.startGame(gameBoard)
    case RefreshMessage(gameBoard, refreshType) => controller.refreshView(gameBoard, refreshType)
    case ConfirmCardPlayMessage()               => controller.confirmCardPlay()
    case StartMenuMessage()                     => controller.startMenu()

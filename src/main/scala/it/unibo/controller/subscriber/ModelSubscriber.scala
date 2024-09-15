package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.ConfirmCardPlayMessage
import it.unibo.controller.ModelMessage
import it.unibo.controller.RefreshMessage
import it.unibo.controller.StartGameMessage
import it.unibo.controller.view.ViewController

/** This class is subscribed to the Model updates and changes the View accordingly */
final class ModelSubscriber(controller: ViewController) extends BaseSubscriber[ModelMessage]:

  override val logger: Logger = Logger("Model -> ModelSubscriber")

  override def onMessageReceived(msg: ModelMessage): Unit = msg match
    case StartGameMessage(gameBoard)            => controller.startGame(gameBoard)
    case RefreshMessage(gameBoard, refreshType) => controller.refreshView(gameBoard, refreshType)
    case ConfirmCardPlayMessage()               => controller.confirmCardPlay()

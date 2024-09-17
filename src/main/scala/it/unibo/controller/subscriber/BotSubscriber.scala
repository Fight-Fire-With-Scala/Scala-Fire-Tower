package it.unibo.controller.subscriber

import com.typesafe.scalalogging.Logger
import it.unibo.controller.BotMessage
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.model.ModelController
import it.unibo.model.effect.phase.PhaseEffect

/** This class is subscribed to the Bot updates and changes the Model accordingly */
final class BotSubscriber(val controller: ModelController) extends BaseSubscriber[BotMessage] with UpdateGamePhaseHandler:

  override val logger: Logger = Logger("Bot -> BotSubscriber")

  override def onMessageReceived(msg: BotMessage): Unit = msg match
    case UpdateGamePhase(ef: PhaseEffect) => handleUpdateGamePhase(ef)
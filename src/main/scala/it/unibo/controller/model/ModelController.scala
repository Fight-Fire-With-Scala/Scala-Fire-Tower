package it.unibo.controller.model

import it.unibo.controller.ModelSubject
import it.unibo.controller.RefreshMessage
import it.unibo.controller.view.RefreshType
import it.unibo.model.ModelModule.Model
import it.unibo.model.effect.core.IGameEffect

final case class ModelController(model: Model, modelObserver: ModelSubject)
    extends PlayerController:

  def applyEffect(ef: IGameEffect, refreshType: RefreshType): Unit =
    val newGb = model.getGameBoard.solveEffect(ef)
    model.setGameBoard(newGb)
    modelObserver.onNext(RefreshMessage(newGb, refreshType))

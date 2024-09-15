package it.unibo.controller.view

trait DiscardCardController extends GameController:
  def confirmDiscard(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.discardCards()
  }

  def cancelDiscard(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.cancelDiscardProcedure()
  }

  def initDiscardProcedure(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.initDiscardProcedure()
  }

  def toggleCardInDiscardList(cardId: Int): Unit = gameComponent.fold(()) { component =>
    component.handComponent.toggleCardInDiscardList(cardId)
  }

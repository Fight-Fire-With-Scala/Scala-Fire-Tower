package it.unibo.controller.view

trait PlayCardController extends GameController:
  def candidateCardToPlay(cardId: Int): Unit = gameComponent.fold(()) { component =>
    component.handComponent.cardToPlay_=(cardId)
  }

  def confirmCardPlay(): Unit = gameComponent.fold(()) { component =>
    component.handComponent.confirmCardPlay()
  }

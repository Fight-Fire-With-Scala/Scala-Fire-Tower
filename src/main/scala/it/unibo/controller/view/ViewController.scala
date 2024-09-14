package it.unibo.controller.view

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.controller.view
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.ViewModule.View
import it.unibo.view.component.game.GameComponent

final case class ViewController(
    view: View,
    internalObservable: InternalViewSubject,
    observable: ViewSubject
) extends DiscardCardController with PlayCardController with RefreshController:

  def startMenu(): Unit = view.startMenu(observable)

  def startGame(gb: GameBoard): Unit =
    given intObservable: InternalViewSubject = internalObservable
    given viewObservable: ViewSubject = observable
    view.startGame(gb, this)

  def refreshView(gb: GameBoard, refreshType: RefreshType): Unit = gameComponent match
    case Some(c) => updateAccordingToRefreshType(c, gb, refreshType)
    case None    => // do not update the view

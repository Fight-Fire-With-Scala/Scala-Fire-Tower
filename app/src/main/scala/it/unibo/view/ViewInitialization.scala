package it.unibo.view

import it.unibo.controller.RefreshType.PhaseUpdate
import it.unibo.controller.view.ViewController
import it.unibo.controller.subscribers.InternalViewSubscriber
import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.IViewComponent
import it.unibo.view.components.game.GameComponent
import monix.eval.Task

object ViewInitialization:
  def getGuiInitTask(
      gameController: ViewController,
      task: Task[IViewComponent],
      gameBoard: GameBoard
  )(using viewObservable: ViewSubject, internalObservable: InternalViewSubject): Task[Unit] = task
    .flatMap { r =>
      val gameComponent = r.asInstanceOf[GameComponent]
      val gameComponentInitTask = GameComponent.initialize(gameComponent)
      gameComponentInitTask.map { gameComponent =>
        logger.debug(s"Game UI initialization completed")
        gameController.initialize(gameComponent)
        val internalViewSubscriber = InternalViewSubscriber(gameController)
        internalObservable.subscribe(internalViewSubscriber)
        gameController.refreshView(gameBoard, PhaseUpdate)
      }
    }

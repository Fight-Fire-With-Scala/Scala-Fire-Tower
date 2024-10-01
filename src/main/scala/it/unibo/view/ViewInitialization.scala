package it.unibo.view

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.controller.subscriber.InternalViewSubscriber
import it.unibo.controller.view.RefreshType.PhaseUpdate
import it.unibo.controller.view.ViewController
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.component.ViewComponent
import it.unibo.view.component.game.GameComponent
import monix.eval.Task

object ViewInitialization:
  def getGuiInitTask(
      gameController: ViewController,
      task: Task[ViewComponent],
      gameBoard: GameBoard
  )(using viewObservable: ViewSubject, internalObservable: InternalViewSubject): Task[Unit] = task
    .flatMap:
      case gc: GameComponent =>
        val gameComponentInitTask = GameComponent.initialize(gc)
        gameComponentInitTask.map: gameComponent =>
          logger.debug("Game UI initialization completed")
          gameController.initialize(gameComponent)
          val internalViewSubscriber = InternalViewSubscriber(gameController)
          internalObservable.subscribe(internalViewSubscriber)
          gameController.refreshView(gameBoard, PhaseUpdate)

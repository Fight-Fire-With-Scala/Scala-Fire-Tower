package it.unibo.view

import it.unibo.controller.RefreshType.PhaseUpdate
import it.unibo.controller.view.InternalViewController
import it.unibo.controller.subscribers.InternalViewSubscriber
import it.unibo.controller.{InternalViewSubject, UpdateGamePhaseModel, ViewSubject}
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.components.IViewComponent
import it.unibo.view.components.game.GameComponent
import monix.eval.Task

object ViewInitialization:
  def getGuiInitTask(
                      gameController: InternalViewController,
                      task: Task[IViewComponent],
                      gameBoard: GameBoard
  )(using viewObservable: ViewSubject, internalObservable: InternalViewSubject): Task[Unit] = task
    .flatMap { r =>
      val gameComponent = r.asInstanceOf[GameComponent]
      val gameComponentInitTask = GameComponent.initialize(gameComponent)
      gameComponentInitTask.map { gameComponent =>
        logger.info(s"Game UI initialization completed")
        gameController.initialize(gameComponent)
        internalObservable.subscribe(InternalViewSubscriber(gameController))
        gameController.refreshView(gameBoard, PhaseUpdate) // just to avoid a flash the first time
        viewObservable
          .onNext(UpdateGamePhaseModel(PhaseEffect(gameBoard.gamePhase))) // get the updated gameboard
        logger.info(s"Wind Direction: ${gameBoard.board.windDirection}")
        logger.info(s"Player turn: ${gameBoard.getCurrentPlayer.name}")
      }
    }

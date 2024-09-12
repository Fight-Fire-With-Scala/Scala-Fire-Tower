package it.unibo.view

import it.unibo.controller.view.ViewController
import it.unibo.controller.{InternalViewSubject, ViewSubject}
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.ViewInitialization.getGuiInitTask
import it.unibo.view.components.IUpdateView
import it.unibo.view.components.game.GameComponent
import monix.execution.Scheduler.Implicits.global

object ViewModule:
  trait View extends IUpdateView:
    def startMenu(viewObservable: ViewSubject): Unit
    def startGame(gb: GameBoard, gameController: ViewController)(using
        intObservable: InternalViewSubject,
        viewObservable: ViewSubject
    ): Unit

  trait Provider:
    val view: View

  trait Component:
    class ViewImpl extends View:
      private var gui: Option[GameUIManager] = None

      override def startMenu(viewObservable: ViewSubject): Unit =
        gui = Some(GameUIManager(1280, 1280, viewObservable))
        gui.get.main(Array.empty)

      override def startGame(gb: GameBoard, gameController: ViewController)(using
          intObservable: InternalViewSubject,
          viewObservable: ViewSubject
      ): Unit =
        val rootTask = gui.get.loadGUIRoot(GameComponent())
        val componentsTask = getGuiInitTask(gameController, rootTask, gb)
        componentsTask.runAsyncAndForget

  trait Interface extends Provider with Component

package it.unibo.view

import scala.compiletime.uninitialized

import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ViewSubject
import it.unibo.controller.view.ViewController
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.ViewInitialization.getGuiInitTask
import it.unibo.view.component.IUpdateView
import it.unibo.view.component.game.GameComponent
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
      private var gui: GameUIManager = uninitialized

      override def startMenu(viewObservable: ViewSubject): Unit =
        gui = GameUIManager(1366, 1000, viewObservable)
        gui.main(Array.empty)

      override def startGame(gb: GameBoard, gameController: ViewController)(using
          intObservable: InternalViewSubject,
          viewObservable: ViewSubject
      ): Unit =
        val rootTask       = gui.loadGUIRoot(GameComponent())
        val componentsTask = getGuiInitTask(gameController, rootTask, gb)
        componentsTask.runAsyncAndForget

  trait Interface extends Provider with Component

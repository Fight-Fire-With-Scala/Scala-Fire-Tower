package it.unibo.view

import it.unibo.controller.view.ViewController
import it.unibo.controller.{InternalViewMessage, InternalViewSubject, ViewMessage, ViewSubject}
import it.unibo.model.gameboard.GameBoard
import it.unibo.view.ViewInitialization.getGuiInitTask
import it.unibo.view.components.IUpdateView
import it.unibo.view.components.game.GameComponent
import monix.reactive.subjects.PublishSubject
import monix.execution.Scheduler.Implicits.global

object ViewModule:
  trait View extends IUpdateView:
    def startMenu(): Unit
    def startGame(gb: GameBoard): Unit
    def getObservable: PublishSubject[ViewMessage]
    def updateView(gb: GameBoard): Unit
    def confirmCardPlay(): Unit

  trait Provider:
    val view: View

  trait Component:
    class ViewImpl extends View:
      private val viewObservable = PublishSubject[ViewMessage]()
      private val intObservable = PublishSubject[InternalViewMessage]()
      private val gui = GameUIManager(1280, 1280, viewObservable)
      private val gameController = ViewController(intObservable, viewObservable)

      override def startMenu(): Unit = gui.main(Array.empty)

      override def startGame(gb: GameBoard): Unit =
        val task = gui.loadGUIRoot(GameComponent())

        given observable: ViewSubject = viewObservable
        given internalObservable: InternalViewSubject = intObservable

        val compositeTask = getGuiInitTask(gameController, task, gb)
        compositeTask.runAsyncAndForget

      override def updateView(gb: GameBoard): Unit = gameController.refreshView(gb)
      override def getObservable: ViewSubject = viewObservable
      override def confirmCardPlay(): Unit = gameController.confirmCardPlay()

  trait Interface extends Provider with Component

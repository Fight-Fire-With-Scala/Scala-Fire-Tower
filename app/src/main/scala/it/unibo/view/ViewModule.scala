package it.unibo.view

import it.unibo.controller.{ControllerModule, ViewMessage, ViewSubject}
import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.components.gameboard.GridComponent
import monix.reactive.subjects.PublishSubject

object ViewModule:

  trait View:
    def show(): Unit
    def startGame(): Unit
    def getObservable: PublishSubject[ViewMessage]
    def refresh(grid: Grid): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:

      private val observableSubject = PublishSubject[ViewMessage]()
      private val gui = MonadicGuiFX(800, 600, new FXMLViewLoader, observableSubject)

      def show(): Unit = gui.main(Array.empty)

      def startGame(): Unit = gui.loadGame()

      def refresh(grid: Grid): Unit = GameBoardController.refresh(grid)

      def getObservable: ViewSubject = observableSubject

  trait Interface extends Provider with Component:
    self: Requirements =>

package it.unibo.view

import it.unibo.controller.{ControllerModule, ViewMessage, ViewSubject}
import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.controllers.GraphicControllerRegistry
import it.unibo.view.controllers.gameboard.GridController
import monix.reactive.subjects.PublishSubject

object ViewModule:

  trait View:
    def show(): Unit
    def showGrid(): Unit
    def updateGrid(grid: Grid): Unit
    def getObservable: PublishSubject[ViewMessage]

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:

      private val observableSubject = PublishSubject[ViewMessage]()
      private val gui =
        MonadicGuiFX(800, 600, new FXMLViewLoader, observableSubject)

      def show(): Unit = gui.main(Array.empty)

      def showGrid(): Unit = gui.loadGrid()

      def updateGrid(grid: Grid): Unit =
        val controller = GraphicControllerRegistry.getController(classOf[GridController])
        controller.foreach(_.updateGrid(grid))

      def getObservable: ViewSubject = observableSubject

  trait Interface extends Provider with Component:
    self: Requirements =>

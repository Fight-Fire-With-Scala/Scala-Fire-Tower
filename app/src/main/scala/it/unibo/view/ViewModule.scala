package it.unibo.view

import it.unibo.controller.{ControllerModule, ViewMessage, ViewSubject}
import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.components.GraphicComponentRegistry
import it.unibo.view.components.gameboard.GridComponent
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
      private val gui = MonadicGuiFX(800, 600, new FXMLViewLoader, observableSubject)

      def show(): Unit = gui.main(Array.empty)

      def showGrid(): Unit = gui.loadGrid()

      def updateGrid(grid: Grid): Unit =
        val component = GraphicComponentRegistry.getComponent(classOf[GridComponent])
        component.foreach(_.updateGrid(grid))

      def getObservable: ViewSubject = observableSubject

  trait Interface extends Provider with Component:
    self: Requirements =>

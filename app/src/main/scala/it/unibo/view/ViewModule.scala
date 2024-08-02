package it.unibo.view

import it.unibo.controller.ControllerModule
import it.unibo.model.gameboard.grid.Grid
import it.unibo.view.controllers.GraphicControllerRegistry
import it.unibo.view.controllers.gameboard.GridController

object ViewModule:

  trait View:
    def show(): Unit
    def showGrid(): Unit
    def updateGrid(grid: Grid): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:

      private val gui = MonadicGuiFX(800, 600, context.controller, new FXMLViewLoader)

      def show(): Unit = gui.main(Array.empty)

      def showGrid(): Unit = gui.loadGrid()

      def updateGrid(grid: Grid): Unit =
        val controller = GraphicControllerRegistry.getController(classOf[GridController])
        controller.foreach(_.updateGrid(grid))

  trait Interface extends Provider with Component:
    self: Requirements =>

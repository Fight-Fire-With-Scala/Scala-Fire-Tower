package it.unibo.view

import it.unibo.controller.ControllerModule
import it.unibo.model.{ModelModule, Observer}
import it.unibo.view.controllers.GraphicControllerRegistry
import it.unibo.view.controllers.gameboard.GridController

object ViewModule:

  trait View extends Observer:
    def show(): Unit
    def showGrid(): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:

      private val gui = MonadicGuiFX(800, 600, context.controller, new FXMLViewLoader)

      def show(): Unit = gui.main(Array.empty)

      override def showGrid(): Unit = gui.loadGrid()

      override def updateGrid(): Unit =
        val controller = GraphicControllerRegistry.getController(classOf[GridController])
        controller.foreach(_.updateGrid(context.controller.getBoard.grid))

  trait Interface extends Provider with Component:
    self: Requirements =>

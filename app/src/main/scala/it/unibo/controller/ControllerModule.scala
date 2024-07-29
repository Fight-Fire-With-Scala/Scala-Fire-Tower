package it.unibo.controller

import it.unibo.model.ModelModule
import it.unibo.model.gameboard.board.Board
import it.unibo.view.ViewModule

object ControllerModule:

  trait Controller:
    def notifyStartMenu(): Unit
    def notifyStartGameSession(): Unit
    def getBoard: Board
  
  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider & ViewModule.Provider

  trait Component:
    context: Requirements =>

    class ControllerImpl extends Controller:

      def notifyStartMenu(): Unit =
        // context.model.init()
        context.view.show()

      def notifyStartGameSession(): Unit =
        context.model.addViewObserver(context.view)
        context.view.showGrid()
        context.model.initialiseModel()

      def getBoard: Board = context.model.getBoard

  trait Interface extends Provider with Component:
    self: Requirements =>

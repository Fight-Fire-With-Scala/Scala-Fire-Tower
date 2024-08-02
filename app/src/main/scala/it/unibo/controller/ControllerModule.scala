package it.unibo.controller

import it.unibo.model.ModelModule
import it.unibo.model.gameboard.board.Board
import it.unibo.view.ViewModule
import it.unibo.subscribers.ModelSubscriber

object ControllerModule:

  trait Controller:
    def notifyStartMenu(): Unit
    def notifyStartGameSession(): Unit
  
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
        context.model.getObservable.subscribe(new ModelSubscriber(context.view))
        context.view.showGrid()
        context.model.initialiseModel()

  trait Interface extends Provider with Component:
    self: Requirements =>

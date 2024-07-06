package it.unibo.view

import it.unibo.controller.ControllerModule

object ViewModule:

  trait View:
    def show(virtualTime: Int): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>
    class ViewImpl extends View:
      private val gui = MonadicGui(800, 500, context.controller)
      def show(virtualTime: Int): Unit = gui `render` virtualTime

  trait Interface extends Provider with Component:
    self: Requirements =>

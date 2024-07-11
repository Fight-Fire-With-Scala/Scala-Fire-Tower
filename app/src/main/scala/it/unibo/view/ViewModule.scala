package it.unibo.view

import it.unibo.controller.ControllerModule

object ViewModule:

  trait View:
    def show(): Unit

  trait Provider:
    val view: View

  type Requirements = ControllerModule.Provider

  trait Component:
    context: Requirements =>

    class ViewImpl extends View:

      private val gui = MonadicGuiFX(800, 500, context.controller)

      def show(): Unit = gui.main(Array.empty)

  trait Interface extends Provider with Component:
    self: Requirements =>

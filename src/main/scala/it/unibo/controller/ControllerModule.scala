package it.unibo.controller

import it.unibo.engine.SimulationEngineModule
import it.unibo.model.ModelModule
import monix.execution.Scheduler.Implicits.global

object ControllerModule:

  trait Controller:
    def notifyStart(): Unit

  trait Provider:
    val controller: Controller

  type Requirements = ModelModule.Provider & SimulationEngineModule.Provider

  trait Component:
    context: Requirements =>
    class ControllerImpl extends Controller:
      def notifyStart(): Unit =
        context.model.init()
        context.simulationEngine
          .simulationStep()
          .loopForever
          .runAsyncAndForget

  trait Interface extends Provider with Component:
    self: Requirements =>

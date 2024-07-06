package it.unibo.engine

import it.unibo.view.ViewModule
import it.unibo.model.ModelModule
import monix.eval.Task

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.language.{implicitConversions, postfixOps}

object SimulationEngineModule:
  
  trait SimulationEngine:
    def simulationStep(): Task[Unit]
    
  trait Provider:
    val simulationEngine: SimulationEngine
    
  type Requirements = ViewModule.Provider & ModelModule.Provider
  
  trait Component:
    context: Requirements =>
    class SimulationEngineImpl extends SimulationEngine:
      
      given unitToTask: Conversion[Unit, Task[Unit]] = Task(_)
      given intToAsk: Conversion[Int, Task[Int]] = Task(_)

      def simulationStep(): Task[Unit] = // this is the step of a simulation, not the entire loop
        for
          _ <- waitFor(1 seconds)
          vt <- computeNewVt()
          _ <- updateModel(vt)
          _ <- updateView()
        yield ()
        
      private def computeNewVt(): Task[Int] = context.model.getVirtualTime + 1
      
      private def updateModel(t: Int): Task[Unit] = context.model.updateVirtualTime(t)
      
      private def updateView(): Task[Unit] =
        val vt = context.model.getVirtualTime
        context.view.show(vt)
        
      private def waitFor(d: FiniteDuration): Task[Unit] = Task.sleep(d)
      
  trait Interface extends Provider with Component:
    self: Requirements =>

package it.unibo.launcher

import it.unibo.controller.ControllerModule
import it.unibo.engine.SimulationEngineModule
import it.unibo.launcher.Launcher.ControllerImpl
import it.unibo.launcher.Launcher.ModelImpl
import it.unibo.launcher.Launcher.SimulationEngineImpl
import it.unibo.launcher.Launcher.ViewImpl
import it.unibo.model.ModelModule
import it.unibo.view.ViewModule

object Launcher
    extends ModelModule.Interface 
    with ViewModule.Interface
    with ControllerModule.Interface
    with SimulationEngineModule.Interface:

  override val model = new ModelImpl()
  override val controller = new ControllerImpl()
  override val view = new ViewImpl()
  override val simulationEngine = new SimulationEngineImpl()

  @main def Main(): Unit =
    println("starting simulation.....")

package it.unibo.launcher

import it.unibo.controller.ControllerModule
import it.unibo.launcher.Launcher.{ControllerImpl, ModelImpl, ViewImpl}
import it.unibo.model.ModelModule
import it.unibo.view.ViewModule

object Launcher
    extends ModelModule.Interface with ViewModule.Interface with ControllerModule.Interface:

  override val model = new ModelImpl()

  override val controller = new ControllerImpl()

  override val view = new ViewImpl()

  @main
  def Main(): Unit = controller.notifyStart()

package it.unibo.view.controllers

import scala.collection.mutable

object GraphicControllerRegistry:
  private val controllers: mutable.Map[Class[_ <: GraphicController], GraphicController] = mutable.Map()

  def register[T <: GraphicController](controller: T): Unit =
    controllers(controller.getClass) = controller

  def getController[T <: GraphicController](controllerClass: Class[T]): Option[T] =
    controllers.get(controllerClass).map(_.asInstanceOf[T])
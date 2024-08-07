package it.unibo.view.components

import scala.collection.mutable

object GraphicComponentRegistry:
  private val components: mutable.Map[Class[_ <: GraphicComponent], GraphicComponent] = mutable.Map()

  def register[T <: GraphicComponent](component: T): Unit =
    components(component.getClass) = component

  def getComponent[T <: GraphicComponent](componentClass: Class[T]): Option[T] =
    components.get(componentClass).map(_.asInstanceOf[T])
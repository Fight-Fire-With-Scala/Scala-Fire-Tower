package it.unibo.view.components

trait IHaveConditionalView extends IHaveView with ICanBeDisabled:
  def generalToggle(): Unit
  
  def enableView(): Unit = if (!enabled) generalToggle()
  def disableView(): Unit = if (enabled) generalToggle()

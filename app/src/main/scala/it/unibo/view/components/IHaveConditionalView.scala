package it.unibo.view.components

trait IHaveConditionalView extends IHaveView with ICanBeDisabled:
  protected var enabled: Boolean = false

  def generalToggle(): Unit =
    enabled = !enabled

  def enableView(): Unit =
    if !enabled then
      generalToggle()

  def disableView(): Unit =
    if enabled then
      generalToggle()

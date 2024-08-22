package it.unibo.view.components.game.gameboard.sidebar.svg

import it.unibo.view.components.ICanBeDisabled
import scalafx.scene.shape.SVGPath

final case class WindRoseDirection(override val svgPath: SVGPath)
    extends IHaveSVGView[WindRoseDirection] with ICanBeDisabled

object WindRoseDirection extends SVGViewFactory[WindRoseDirection]:
  override protected def createInstance(svgPath: SVGPath): WindRoseDirection =
    WindRoseDirection(svgPath)

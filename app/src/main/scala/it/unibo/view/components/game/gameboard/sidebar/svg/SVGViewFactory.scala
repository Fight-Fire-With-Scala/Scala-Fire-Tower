package it.unibo.view.components.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction
import scalafx.scene.shape.SVGPath

trait SVGViewFactory[T <: IHaveSVGView[?]]:
  def create(direction: Direction): T =
    val svgPath: SVGPath = new SVGPath()
    val instance = createInstance(svgPath)
    instance.getSVGFromDirection(direction)
    instance

  protected def createInstance(svgPath: SVGPath): T

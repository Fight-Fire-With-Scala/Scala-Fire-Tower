package it.unibo.view.components.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction
import it.unibo.view.components.game.gameboard.sidebar.svg.SVGCommonPaths.getPathFromDirection
import scalafx.scene.shape.SVGPath

final case class DiceFace(override val svgPath: SVGPath) extends UpdatableSVG[DiceFace]:
  override def updateDirection(direction: Direction): DiceFace =
    val path = getPathFromDirection(direction)
    svgPath.setContent(path)
    copy(svgPath)

object DiceFace extends SVGViewFactory[DiceFace]:
  override protected def createInstance(svgPath: SVGPath): DiceFace = DiceFace(svgPath)

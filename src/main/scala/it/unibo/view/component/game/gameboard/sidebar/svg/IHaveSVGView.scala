package it.unibo.view.component.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction
import it.unibo.view.component.game.gameboard.sidebar.svg.IHaveSVGView.color
import it.unibo.view.component.game.gameboard.sidebar.svg.IHaveSVGView.scaleFactor
import it.unibo.view.component.game.gameboard.sidebar.svg.SVGCommonPaths.getPathFromDirection
import scalafx.scene.paint.Color
import scalafx.scene.shape.SVGPath

trait IHaveSVGView[T]:
  val svgPath: SVGPath
  def getSVGFromDirection(direction: Direction): SVGPath =
    val path = getPathFromDirection(direction)
    svgPath.setContent(path)
    svgPath.setFill(color)
    svgPath.setScaleX(scaleFactor)
    svgPath.setScaleY(scaleFactor)
    svgPath

object IHaveSVGView:
  private val scaleFactor = 25.0
  private val color       = Color.web("#1b2b4c")

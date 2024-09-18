package it.unibo.view.component.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.East
import it.unibo.model.gameboard.Direction.North
import it.unibo.model.gameboard.Direction.South
import it.unibo.model.gameboard.Direction.West
import it.unibo.view.component.ICanBeDisabled
import it.unibo.view.component.game.gameboard.sidebar.svg.WindRoseArrow.getRotationFromDirection
import javafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.scene.shape.SVGPath

final case class WindRoseArrow(override val svgPath: SVGPath)
    extends UpdatableSVG[WindRoseArrow]
    with ICanBeDisabled:
  override def updateDirection(direction: Direction): WindRoseArrow =
    val rotationDegree = getRotationFromDirection(direction)
    svgPath.rotate = rotationDegree
    copy(svgPath)

  override def getSVGFromDirection(direction: Direction): SVGPath =
    val rotationDegree = getRotationFromDirection(direction)
    svgPath.setFill(WindRoseArrow.color)
    svgPath.setScaleX(WindRoseArrow.scaleFactor)
    svgPath.setScaleY(WindRoseArrow.scaleFactor)
    svgPath.setContent(WindRoseArrow.path)
    svgPath.rotate = rotationDegree
    svgPath

  override def onEnableView(): Unit = svgPath.setOpacity(0.9)

  override def onDisableView(): Unit = svgPath.setOpacity(0.7)

  override protected def getPane: Node = svgPath

object WindRoseArrow extends SVGViewFactory[WindRoseArrow]:
  override protected def createInstance(svgPath: SVGPath): WindRoseArrow = WindRoseArrow(svgPath)

  private val scaleFactor = 5.5
  private val color       = Color.web("#1b2b4c")
  private val path =
    "M16.646 15.646L19.293 13H3v-1h16.293l-2.647-2.646.707-.707 3.854 3.853-3.854 3.854z"

  private def getRotationFromDirection(direction: Direction): Double = direction match
    case North => 0.0
    case South => 180.0
    case East  => 90.0
    case West  => -90.0

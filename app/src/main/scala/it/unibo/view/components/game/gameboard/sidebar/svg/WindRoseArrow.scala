package it.unibo.view.components.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.East
import it.unibo.model.gameboard.Direction.North
import it.unibo.model.gameboard.Direction.South
import it.unibo.model.gameboard.Direction.West
import it.unibo.view.components.ICanBeDisabled
import it.unibo.view.components.game.gameboard.sidebar.svg.WindRoseArrow.getRotationFromDirection
import javafx.scene.Node
import scalafx.scene.paint.Color
import scalafx.scene.shape.SVGPath

final case class WindRoseArrow(override val svgPath: SVGPath)
    extends UpdatableSVG[WindRoseArrow] with ICanBeDisabled:
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

  private val scaleFactor = 3.0
  private val color = Color.web("#1b2b4c")
  private val path =
    "m 26.71,10.29 -10,-10 c -0.390037,-0.38772359 -1.019963,-0.38772359 -1.41,0 l -10,10 3.2045278,2.9394 4.9921452,-5.0953656 -0.06416,23.9237986 5.004721,0.04002 -0.06964,-24.0511587 5.155021,5.2898927 z"

  private def getRotationFromDirection(direction: Direction): Double = direction match
    case North => 0.0
    case South => 180.0
    case East  => 90.0
    case West  => -90.0

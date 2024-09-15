package it.unibo.view.component.game.gameboard.sidebar.svg

import it.unibo.view.component.ICanBeDisabled
import javafx.scene.Node
import scalafx.scene.shape.SVGPath

final case class WindRoseDirection(override val svgPath: SVGPath)
    extends IHaveSVGView[WindRoseDirection]
    with ICanBeDisabled:

  override def onEnableView(): Unit = svgPath.setOpacity(0.9)

  override def onDisableView(): Unit = svgPath.setOpacity(0.7)

  override protected def getPane: Node = svgPath

object WindRoseDirection extends SVGViewFactory[WindRoseDirection]:
  override protected def createInstance(svgPath: SVGPath): WindRoseDirection =
    WindRoseDirection(svgPath)

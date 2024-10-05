package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import it.unibo.model.gameboard
import it.unibo.model.gameboard.Direction
import it.unibo.view.GUIType
import it.unibo.view.component.{ IUpdateView, ViewComponent }
import it.unibo.view.component.game.gameboard.sidebar.windrose.{ WindRoseComponentProxy, WindRoseComponentService }
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane

//noinspection VarCouldBeVal
final class WindRoseComponent(wrc: WindRoseComponentProxy)
    extends WindRoseComponentService
    with ViewComponent
    with IUpdateView:
  override val fxmlPath: String = GUIType.WindRose.fxmlPath

  @FXML
  private var basePane: Pane = uninitialized

  @FXML
  private var northPane, westPane, centerPane, eastPane, southPane: Pane = uninitialized

  @FXML
  def initialize(): Unit = wrc.initialize(northPane, westPane, centerPane, eastPane, southPane)

  def updateWindRoseDirection(direction: Direction): Unit =
    runOnUIThread(wrc.updateWindRoseDirection(direction))

  def onWindDirectionRequest(direction: Direction): Unit =
    runOnUIThread(wrc.onWindDirectionRequest(direction))

  def allowInteraction(direction: Direction): Unit =
    wrc.allowInteraction(basePane, direction)

  def disallowInteraction(): Unit = wrc.disallowInteraction(basePane)

  override def onEnableView(): Unit =
    super.onEnableView()
    wrc.onEnableView()

  override def onDisableView(): Unit =
    super.onDisableView()
    wrc.onDisableView()

  override protected def getPane: Node = basePane

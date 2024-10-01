package it.unibo.view.component.game.gameboard.sidebar

import it.unibo.view.GUIType
import it.unibo.view.component.ViewComponent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class BicolumnComponent(
                               val leftComponent: ViewComponent,
                               val rightComponent: ViewComponent
) extends ViewComponent:
  override val fxmlPath: String = GUIType.BicolumnPane.fxmlPath

  @FXML
  private var basePane: Pane = uninitialized

  @FXML
  private var leftPane: Pane = uninitialized

  @FXML
  private var rightPane: Pane = uninitialized

  @FXML
  def initialize(): Unit =
    leftPane.getChildren.add(leftComponent.getView)
    rightPane.getChildren.add(rightComponent.getView)

  override protected def onEnableView(): Unit =
    leftComponent.enableView()
    rightComponent.enableView()

  override protected def onDisableView(): Unit =
    leftComponent.disableView()
    rightComponent.disableView()

  override protected def getPane: Node = basePane

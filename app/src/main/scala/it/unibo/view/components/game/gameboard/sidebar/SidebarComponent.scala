package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.view.GUIType
import it.unibo.view.components.{ISidebarComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.VBox

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class SidebarComponent(val components: List[ISidebarComponent])
    extends ISidebarComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Sidebar.fxmlPath

  @FXML
  private var sidebarPane: VBox = uninitialized

  private val addComponent =
    (component: ISidebarComponent) => sidebarPane.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit =
    components.foreach(addComponent)
    enableView()

  override def onEnableView(): Unit =
    components.foreach(c => c.enableView())

  override def onDisableView(): Unit =
    components.foreach(c => c.disableView())

  override protected def getPane: Node = sidebarPane

package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import it.unibo.view.GUIType
import it.unibo.view.component.{ IUpdateView, ViewComponent }
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.VBox

//noinspection VarCouldBeVal
final class SidebarComponent(val components: List[ViewComponent])
    extends ViewComponent
    with IUpdateView:

  override val fxmlPath: String = GUIType.Sidebar.fxmlPath

  @FXML
  private var sidebarPane: VBox = uninitialized

  private val addComponent =
    (component: ViewComponent) => sidebarPane.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit =
    components.foreach(addComponent)
    enableView()

  override def onEnableView(): Unit =
    components.foreach(c => c.enableView())

  override def onDisableView(): Unit =
    components.foreach(c => c.disableView())

  override protected def getPane: Node = sidebarPane

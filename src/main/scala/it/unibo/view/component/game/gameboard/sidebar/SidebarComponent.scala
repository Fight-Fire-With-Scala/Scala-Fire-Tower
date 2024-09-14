package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import it.unibo.view.{GUIType, logger}
import it.unibo.view.component.ISidebarComponent
import it.unibo.view.component.IUpdateView
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.VBox

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
    logger.info(s"[ACTIVATION] Enabled ${this.getClass.getSimpleName}")
    components.foreach(c => c.enableView())

  override def onDisableView(): Unit =
    logger.info(s"[ACTIVATION] Disabled ${this.getClass.getSimpleName}")
    components.foreach(c => c.disableView())

  override protected def getPane: Node = sidebarPane

package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.view.GUIType
import it.unibo.view.components.{
  IMainComponent,
  ISidebarComponent,
  IUpdateView
}
import javafx.fxml.FXML
import javafx.scene.layout.VBox

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class SidebarComponent(val components: List[ISidebarComponent])
    extends IMainComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Sidebar.fxmlPath

  @FXML
  private var sidebarContainer: VBox = uninitialized

  private val addComponent =
    (component: ISidebarComponent) => sidebarContainer.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit = components.foreach(addComponent)

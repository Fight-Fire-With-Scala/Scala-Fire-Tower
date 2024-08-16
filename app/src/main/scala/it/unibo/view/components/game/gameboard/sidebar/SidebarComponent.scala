package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.view.components.{GraphicComponent, IHaveView}
import javafx.fxml.FXML
import javafx.scene.layout.VBox

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class SidebarComponent(val components: List[IHaveView]) extends GraphicComponent:

  @FXML
  private var sidebarContainer: VBox = uninitialized

  private val addComponent =
    (component: IHaveView) => sidebarContainer.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit = components.foreach(addComponent)

package it.unibo.view

import it.unibo.view.components.GraphicComponent
import it.unibo.view.components.game.GameComponent
import it.unibo.view.components.menu.MenuComponent

sealed trait RootView:
  type Component <: GraphicComponent
  val fxmlPath: String

sealed trait MenuRootView extends RootView:
  override type Component = MenuComponent

sealed trait GameRootView extends RootView:
  override type Component = GameComponent
  
enum GUIType(val fxmlPath: String):
  case Menu extends GUIType("/pages/menu.fxml") with MenuRootView
  case Game extends GUIType("/pages/game.fxml") with GameRootView
  case Hand extends GUIType("/pages/hand.fxml")
  case Grid extends GUIType("/pages/grid.fxml")
  case Sidebar extends GUIType("/pages/sidebar.fxml")
  case Deck extends GUIType("/pages/deck.fxml")
  case GameInfo extends GUIType("/pages/gameInfo.fxml")
  case WindRose extends GUIType("/pages/windRose.fxml")
  case Card extends GUIType("/pages/card.fxml")

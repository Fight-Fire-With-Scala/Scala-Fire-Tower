package it.unibo.view

enum GUIType(val fxmlPath: String):
  case Menu extends GUIType("/pages/menu.fxml")
  case Hand extends GUIType("/pages/hand.fxml")
  case Grid extends GUIType("/pages/grid.fxml")
  case Game extends GUIType("/pages/game.fxml")
  case Sidebar extends GUIType("/pages/sidebar.fxml")

package it.unibo.view

enum GUIType(val fxmlPath: String):
  case Menu extends GUIType("/pages/menu.fxml")
  case Game extends GUIType("/pages/game.fxml")
  case Hand extends GUIType("/pages/hand.fxml")
  case Grid extends GUIType("/pages/grid.fxml")
  case Sidebar extends GUIType("/pages/sidebar.fxml")
  case Deck extends GUIType("/pages/deck.fxml")
  case GameInfo extends GUIType("/pages/gameInfo.fxml")
  case WindRose extends GUIType("/pages/windRose.fxml")
  case Card extends GUIType("/pages/card.fxml")

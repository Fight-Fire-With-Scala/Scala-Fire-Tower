package it.unibo.view

enum GUIType(val fxmlPath: String):
  case Menu extends GUIType("/pages/menu.fxml")
  case Hand extends GUIType("/pages/hand.fxml")
  case Card extends GUIType("/pages/card.fxml")

package it.unibo.view.component

import it.unibo.view.FXMLViewLoader
import javafx.scene.Node

trait IHaveView:
  val fxmlPath: String
  def getView: Node = FXMLViewLoader.load(this)

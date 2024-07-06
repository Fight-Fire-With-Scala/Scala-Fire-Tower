package it.unibo

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, Circle, Rectangle}

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene {
        root = new BorderPane {
          padding = Insets(25)
          center = new Label("Hello SBT")
        }
      }
    }
  }
}
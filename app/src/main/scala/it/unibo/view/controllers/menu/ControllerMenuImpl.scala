package it.unibo.view.controllers.menu

import it.unibo.view.controllers.utils.*
import it.unibo.controller.ControllerModule.given_StartGameNotifier
import javafx.fxml.FXML
import javafx.scene.control.{Button, RadioButton, TextField, ToggleGroup}
import scalafx.Includes.*
import scalafx.application.Platform

import scala.compiletime.uninitialized

class ControllerMenuImpl {

  @FXML
  private var humanVsHuman: RadioButton = uninitialized

  @FXML
  private var player2Input: TextField = uninitialized

  @FXML
  private var gameModeToggleGroup: ToggleGroup = uninitialized

  @FXML
  def initialize(): Unit = gameModeToggleGroup.selectedToggleProperty()
    .addListener((_, _, newToggle) => player2Input.setDisable(newToggle != humanVsHuman))

  @FXML
  def handleExitAction(): Unit = {
    Platform.exit()
    System.exit(0)
  }

  @FXML
  def handleStartAction(): Unit = {this.notifyGameStart()}

}
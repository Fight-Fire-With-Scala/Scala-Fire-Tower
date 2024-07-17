package it.unibo.view.controllers.menu

import it.unibo.view.controllers.utils.Notifier
import javafx.fxml.FXML
import javafx.scene.control.{Button, RadioButton, TextField, ToggleGroup}
import scalafx.Includes.*
import scalafx.application.Platform

import scala.compiletime.uninitialized

trait ControllerService

trait MenuControllerService extends ControllerService:
  def handleStartAction(): Unit
  def handleExitAction(): Unit

//class MenuControllerProxy(actualController: MenuControllerService) extends MenuControllerService:
//  override def handleStartAction(): Unit = actualController.handleStartAction()
//  override def handleExitAction(): Unit = actualController.handleExitAction()

//noinspection VarCouldBeVal
class MenuController(notifier: Notifier) extends MenuControllerService:

  @FXML
  private var humanVsHuman: RadioButton = uninitialized

  @FXML
  private var player2Input: TextField = uninitialized

  @FXML
  private var gameModeToggleGroup: ToggleGroup = uninitialized

  @FXML
  private var exitButton: Button = uninitialized

  @FXML
  def initialize(): Unit = gameModeToggleGroup.selectedToggleProperty()
    .addListener((_, _, newToggle) => player2Input.setDisable(newToggle != humanVsHuman))

  @FXML
  def handleExitAction(): Unit =
    Platform.exit()
    System.exit(0)

  @FXML
  def handleStartAction(): Unit = notifier.doNotify()

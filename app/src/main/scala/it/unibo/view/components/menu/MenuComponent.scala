package it.unibo.view.components.menu

import it.unibo.model.settings.{BotBehaviour, CardSet, GameMode, Settings}
import it.unibo.view.components.IMainComponent
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, RadioButton, TextField, ToggleGroup}
import scalafx.Includes.*
import scalafx.application.Platform
import it.unibo.controller.{SettingsMessage, ViewSubject}
import it.unibo.view.GUIType

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class MenuComponent(observableSubject: ViewSubject) extends IMainComponent:
  override val fxmlPath: String = GUIType.Menu.fxmlPath
  
  @FXML
  private var gameModeToggleGroup: ToggleGroup = uninitialized
  @FXML
  private var humanVsHuman: RadioButton = uninitialized
  @FXML
  private var humanVsBot: RadioButton = uninitialized
  @FXML
  private var setOfCardsDropdown: ComboBox[CardSet] = uninitialized
  @FXML
  private var botBehaviourDropdown: ComboBox[BotBehaviour] = uninitialized
  @FXML
  private var exitButton: Button = uninitialized
  @FXML
  private var player1Input: TextField = uninitialized
  @FXML
  private var player2Input: TextField = uninitialized

  @FXML
  def initialize(): Unit =
    setOfCardsDropdown
      .setItems(javafx.collections.FXCollections.observableArrayList(CardSet.values*))
    botBehaviourDropdown
      .setItems(javafx.collections.FXCollections.observableArrayList(BotBehaviour.values*))
    botBehaviourDropdown.setDisable(true)
    gameModeToggleGroup.selectedToggleProperty().addListener { (_, _, newToggle) =>
      val isHumanVsHumanSelected = newToggle == humanVsHuman
      player2Input.setDisable(!isHumanVsHumanSelected)
      botBehaviourDropdown.setDisable(isHumanVsHumanSelected)
    }

  @FXML
  def handleExitAction(): Unit =
    Platform.exit()
    System.exit(0)

  @FXML
  def handleStartAction(): Unit =
    val selectedGameMode =
      if (humanVsHuman.isSelected) GameMode.HumanVsHuman else GameMode.HumanVsBot
    val selectedCardSet = Option(setOfCardsDropdown.getValue).getOrElse(CardSet.Base)
    val selectedBotBehaviour =
      if (humanVsHuman.isSelected) None
      else Some(Option(botBehaviourDropdown.getValue).getOrElse(BotBehaviour.Balanced))
    val playerOneNameInput =
      if (player1Input.getText.trim.isEmpty) "Player 1" else player1Input.getText.trim
    val playerTwoNameInput = Option(player2Input.getText).filterNot(_.trim.isEmpty)

    observableSubject.onNext(SettingsMessage(Settings(
      gameMode = selectedGameMode,
      cardSet = selectedCardSet,
      botBehaviour = selectedBotBehaviour,
      playerOneName = playerOneNameInput,
      playerTwoName = playerTwoNameInput
    )))

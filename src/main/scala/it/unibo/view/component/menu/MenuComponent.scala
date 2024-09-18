package it.unibo.view.component.menu

import scala.compiletime.uninitialized

import it.unibo.controller.GameBoardInitializationMessage
import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.GameBoardConfig
import it.unibo.model.gameboard.GameBoardConfig.BotBehaviour
import it.unibo.model.gameboard.GameBoardConfig.CardSet
import it.unibo.model.gameboard.GameBoardConfig.GameMode
import it.unibo.view.GUIType
import it.unibo.view.component.IViewComponent
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.Pane
import scalafx.Includes._
import scalafx.application.Platform

//noinspection VarCouldBeVal
final class MenuComponent(observableSubject: ViewSubject) extends IViewComponent:
  override val fxmlPath: String = GUIType.Menu.fxmlPath

  @FXML
  private var menuPane: Pane = uninitialized
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
      .setItems(
        javafx.collections.FXCollections.observableArrayList(GameBoardConfig.CardSet.values*)
      )
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
    val selectedCardSet =
      Option(setOfCardsDropdown.getValue).getOrElse(GameBoardConfig.CardSet.Base)
    val selectedBotBehaviour =
      if (humanVsHuman.isSelected) None
      else Some(Option(botBehaviourDropdown.getValue).getOrElse(BotBehaviour.Balanced))
    val playerOneNameInput =
      if (player1Input.getText.trim.isEmpty) "Player 1" else player1Input.getText.trim
    val playerTwoNameInput = Option(player2Input.getText).filterNot(_.trim.isEmpty)

    observableSubject.onNext(
      GameBoardInitializationMessage(
        GameBoardConfig(
          gameMode = selectedGameMode,
          cardSet = selectedCardSet,
          botBehaviour = selectedBotBehaviour,
          playerOneName = playerOneNameInput,
          playerTwoName = playerTwoNameInput
        )
      )
    )

  override protected def getPane: Node = menuPane

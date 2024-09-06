package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.ViewSubject
import it.unibo.view.GUIType
import it.unibo.view.components.{ISidebarComponent, IUpdateView}

import scala.compiletime.uninitialized
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.control.TextField

//noinspection VarCouldBeVal
final class GameInfoComponent(using observable: ViewSubject)
    extends ISidebarComponent with IUpdateView:
  override val fxmlPath: String = GUIType.GameInfo.fxmlPath

  @FXML
  private var gameInfoPane: Pane = uninitialized

  @FXML
  private var turnNumber, turnPlayer, turnPhase: TextField = uninitialized

  @FXML
  def initialize(): Unit =
    turnNumber.setEditable(false)
    turnPlayer.setEditable(false)
    turnPhase.setEditable(false)

  def updateTurnNumber(currentTurnNumber: Int): Unit =
    runOnUIThread(turnNumber.setText(s"Turn Number: $currentTurnNumber"))
  def updateTurnPlayer(currentPlayer: String): Unit =
    runOnUIThread(turnPlayer.setText(s"Player: $currentPlayer"))
  def updateTurnPhase(currentTurnPhase: String): Unit =
    runOnUIThread(turnPhase.setText(s"Phase: $currentTurnPhase"))

  override protected def getPane: Node = gameInfoPane

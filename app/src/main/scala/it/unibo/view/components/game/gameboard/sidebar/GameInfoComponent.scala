package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.{InternalViewSubject, UpdateGamePhase, ViewSubject}
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.EndTurnPhase
import it.unibo.view.GUIType
import it.unibo.view.components.{ISidebarComponent, IUpdateView}
import javafx.event.EventHandler

import scala.compiletime.uninitialized
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.control.{Button, TextField}
import javafx.scene.input.MouseEvent

//noinspection VarCouldBeVal
final class GameInfoComponent(using
    observable: ViewSubject,
    internalObservable: InternalViewSubject
) extends ISidebarComponent with IUpdateView:
  override val fxmlPath: String = GUIType.GameInfo.fxmlPath

  @FXML
  private var gameInfoPane: Pane = uninitialized

  @FXML
  private var turnNumber, turnPlayer, turnPhase: TextField = uninitialized

  @FXML
  private var endTurnButton: Button = uninitialized

  private val endTurnButtonEventHandler: EventHandler[MouseEvent] =
    (_: MouseEvent) => observable.onNext(UpdateGamePhase(PhaseEffect(EndTurnPhase)))

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

  override def onEnableView(): Unit =
    super.onEnableView()
    endTurnButton.addEventHandler(MouseEvent.MOUSE_CLICKED, endTurnButtonEventHandler)

  override def onDisableView(): Unit =
    super.onDisableView()
    endTurnButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, endTurnButtonEventHandler)

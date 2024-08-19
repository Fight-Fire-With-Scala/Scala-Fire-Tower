package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.ViewSubject
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.South
import it.unibo.view.GUIType
import it.unibo.view.components.game.gameboard.sidebar.svg.DiceFace
import it.unibo.view.components.{ICanBeDisabled, IHaveView, IUpdateView}
import javafx.event.EventHandler

import scala.compiletime.uninitialized
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.input.MouseEvent
import javafx.scene.control.TextField
import scalafx.scene.shape.SVGPath

import scala.util.Random

//noinspection VarCouldBeVal
final class GameInfoComponent(using observable: ViewSubject)
    extends IHaveView with ICanBeDisabled with IUpdateView:
  override val fxmlPath: String = GUIType.GameInfo.fxmlPath

  @FXML
  private var dicePane: Pane = uninitialized

  @FXML
  private var turnNumber, turnPlayer, turnPhase: TextField = uninitialized

  private val diceFace = DiceFace.create(South)

  @FXML
  def initialize(): Unit =
    turnNumber.setEditable(false)
    turnPlayer.setEditable(false)
    turnPhase.setEditable(false)

    dicePane.getChildren.add(diceFace.svgPath)
    toggleActivation()

  private val diceEventHandler: EventHandler[MouseEvent] =
    ev => diceFace.updateDirection(Random.shuffle(Direction.values).head)

  def updateDiceFaceDirection(direction: Direction): Unit =
    runOnUIThread(diceFace.updateDirection(direction))
  def updateTurnNumber(currentTurnNumber: Int): Unit =
    runOnUIThread(turnNumber.setText(s"Turn Number: $currentTurnNumber"))
  def updateTurnPlayer(currentPlayer: String): Unit =
    runOnUIThread(turnPlayer.setText(s"Player: $currentPlayer"))
  def updateTurnPhase(currentTurnPhase: String): Unit =
    runOnUIThread(turnPhase.setText(s"Phase: $currentTurnPhase"))

  def toggleActivation(): Unit =
    super.toggleActivation(
      dicePane,
      List(MouseEvent.MOUSE_CLICKED -> diceEventHandler),
      () => dicePane.getStyleClass.add("disabled"),
      () => dicePane.getStyleClass.remove("disabled")
    )

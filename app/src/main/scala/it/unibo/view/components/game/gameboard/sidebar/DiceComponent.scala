package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.{UpdateGamePhaseModel, UpdateWindDirection, ViewSubject}
import it.unibo.model.effects.cards.WindChoiceEffect
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.view.GUIType
import it.unibo.view.components.{ISidebarComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.South
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.view.components.game.gameboard.sidebar.svg.DiceFace
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import scalafx.scene.shape.SVGPath

import scala.util.Random

//noinspection VarCouldBeVal
final class DiceComponent(using observable: ViewSubject) extends ISidebarComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Dice.fxmlPath

  @FXML
  private var dicePane: Pane = uninitialized

  @FXML
  def initialize(): Unit = dicePane.getChildren.add(diceFace.svgPath)

  private val diceFace = DiceFace.create(South)

  private val diceEventHandler: EventHandler[MouseEvent] = ev => diceClickHandler()

  private def diceClickHandler(): Unit =
    val updatedDirection = Random.shuffle(Direction.values).head
    diceFace.updateDirection(updatedDirection)
    observable.onNext(UpdateWindDirection(WindChoiceEffect.RandomUpdateWind))
    observable.onNext(UpdateGamePhaseModel(PhaseEffect(PlaySpecialCardPhase)))

  def updateDiceFaceDirection(direction: Direction): Unit =
    runOnUIThread(diceFace.updateDirection(direction))

  override def onEnableView(): Unit =
    super.onEnableView()
    diceFace.svgPath.setOpacity(0.9)
    dicePane.addEventHandler(MouseEvent.MOUSE_CLICKED, diceEventHandler)

  override def onDisableView(): Unit =
    super.onDisableView()
    diceFace.svgPath.setOpacity(0.7)
    dicePane.removeEventHandler(MouseEvent.MOUSE_CLICKED, diceEventHandler)

  override protected def getPane: Node = dicePane

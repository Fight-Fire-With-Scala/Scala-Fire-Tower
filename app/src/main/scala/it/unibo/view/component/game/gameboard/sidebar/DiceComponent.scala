package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import scala.util.Random

import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.UpdateWindDirection
import it.unibo.controller.ViewSubject
import it.unibo.model.effect.card.WindChoiceEffect
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.South
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.view.GUIType
import it.unibo.view.component.ISidebarComponent
import it.unibo.view.component.IUpdateView
import it.unibo.view.component.game.gameboard.sidebar.svg.DiceFace
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import scalafx.scene.shape.SVGPath

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
    observable.onNext(UpdateGamePhase(PhaseEffect(PlaySpecialCardPhase)))

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

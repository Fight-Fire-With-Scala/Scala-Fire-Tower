package it.unibo.view.component.game.gameboard.sidebar

import scala.compiletime.uninitialized
import it.unibo.controller.CancelDiscardMessage
import it.unibo.controller.ConfirmDiscardMessage
import it.unibo.controller.InitializeDiscardProcedureMessage
import it.unibo.controller.InternalViewSubject
import it.unibo.controller.UpdateGamePhaseMessage
import it.unibo.controller.ViewSubject
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.view.GUIType
import it.unibo.view.component.ISidebarComponent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

//noinspection VarCouldBeVal
final class DeckComponent(using observable: ViewSubject, internalObservable: InternalViewSubject)
    extends ISidebarComponent:

  @FXML
  private var deckPane: Pane = uninitialized

  @FXML
  private var discardButton: Button = uninitialized

  @FXML
  private var okButton: Button = uninitialized

  @FXML
  private var cancelButton: Button = uninitialized

  private var maxCards: Int = 5

  override val fxmlPath: String = GUIType.Deck.fxmlPath

  private val okButtonEventHandler: EventHandler[MouseEvent] = (_: MouseEvent) => discard()

  private val cancelButtonEventHandler: EventHandler[MouseEvent] = (_: MouseEvent) => cancel()

  private val discardButtonEventHandler: EventHandler[MouseEvent] =
    (_: MouseEvent) => startDiscardProcedure()

  private def addHandlers(): Unit =
    okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, okButtonEventHandler)
    cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, cancelButtonEventHandler)
    discardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, discardButtonEventHandler)

  @FXML
  private def initialize(): Unit =
    setDiscardProcedureButtonsEnabled(false)
    okButton.setFocusTraversable(false)
    cancelButton.setFocusTraversable(false)
    discardButton.setFocusTraversable(false)
    addHandlers()

  private def setDiscardProcedureButtonsEnabled(enabled: Boolean): Unit =
    okButton.setDisable(!enabled)
    cancelButton.setDisable(!enabled)
    discardButton.setDisable(enabled)

  private def startDiscardProcedure(): Unit =
    internalObservable.onNext(InitializeDiscardProcedureMessage())
    observable.onNext(UpdateGamePhaseMessage(PhaseEffect(GamePhase.RedrawCardsPhase)))
    setDiscardProcedureButtonsEnabled(true)

  private def discard(): Unit =
    internalObservable.onNext(ConfirmDiscardMessage())
    setDiscardProcedureButtonsEnabled(false)

  private def cancel(): Unit =
    internalObservable.onNext(CancelDiscardMessage())
    setDiscardProcedureButtonsEnabled(false)

  override protected def onEnableView(): Unit =
    super.onEnableView()
    addHandlers()

  override protected def onDisableView(): Unit =
    super.onDisableView()
    okButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, okButtonEventHandler)
    cancelButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, cancelButtonEventHandler)
    discardButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, discardButtonEventHandler)

  override protected def getPane: Node = deckPane

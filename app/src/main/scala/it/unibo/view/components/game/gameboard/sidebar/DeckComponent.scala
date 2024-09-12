package it.unibo.view.components.game.gameboard.sidebar

import scala.compiletime.uninitialized

import it.unibo.controller.CancelDiscardMessage
import it.unibo.controller.ConfirmDiscardMessage
import it.unibo.controller.DrawCardMessage
import it.unibo.controller.InitializeDiscardProcedureMessage
import it.unibo.controller.InternalViewSubject
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.ViewSubject
import it.unibo.model.effects.hand.HandEffect.DrawCard
import it.unibo.model.effects.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.view.GUIType
import it.unibo.view.components.ISidebarComponent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

//noinspection VarCouldBeVal
final class DeckComponent(using observable: ViewSubject, internalObservable: InternalViewSubject)
    extends ISidebarComponent:

  @FXML
  private var deckPane: Pane = uninitialized

  @FXML
  private var drawButton: Button = uninitialized

  @FXML
  private var numberInput: Spinner[Integer] = uninitialized

  @FXML
  private var discardButton: Button = uninitialized

  @FXML
  private var okButton: Button = uninitialized

  @FXML
  private var cancelButton: Button = uninitialized

  private var maxCards: Int = 5

  override val fxmlPath: String = GUIType.Deck.fxmlPath

  def maxCards_=(value: Int): Unit =
    maxCards = value
    updateSpinnerValueFactory()

  private val spinnerEventHandler: EventHandler[MouseEvent] =
    (_: MouseEvent) => updateSpinnerValueFactory()

  private val okButtonEventHandler: EventHandler[MouseEvent] = (_: MouseEvent) => discard()

  private val cancelButtonEventHandler: EventHandler[MouseEvent] = (_: MouseEvent) => cancel()

  private val discardButtonEventHandler: EventHandler[MouseEvent] =
    (_: MouseEvent) => startDiscardProcedure()

  private val drawButtonEventHandler: EventHandler[MouseEvent] = (_: MouseEvent) => handleDrawCard()

  private val handlers = Seq(
    spinnerEventHandler,
    okButtonEventHandler,
    cancelButtonEventHandler,
    discardButtonEventHandler,
    drawButtonEventHandler
  )

  private def addHandlers(): Unit =
    numberInput.addEventHandler(MouseEvent.MOUSE_CLICKED, spinnerEventHandler)
    okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, okButtonEventHandler)
    cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, cancelButtonEventHandler)
    discardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, discardButtonEventHandler)
    drawButton.addEventHandler(MouseEvent.MOUSE_CLICKED, drawButtonEventHandler)

  @FXML
  private def initialize(): Unit =
    updateSpinnerValueFactory()
    setDiscardProcedureButtonsEnabled(false)
    addHandlers()

  private def updateSpinnerValueFactory(): Unit =
    val valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCards, 1)
    numberInput.setValueFactory(valueFactory)
    numberInput.getValueFactory.setValue(1)

  private def setDiscardProcedureButtonsEnabled(enabled: Boolean): Unit =
    okButton.setDisable(!enabled)
    cancelButton.setDisable(!enabled)
    discardButton.setDisable(enabled)

  private def handleDrawCard(): Unit = observable
    .onNext(DrawCardMessage(DrawCard(numberInput.getValue)))

  private def startDiscardProcedure(): Unit =
    internalObservable.onNext(InitializeDiscardProcedureMessage())
    observable.onNext(UpdateGamePhase(PhaseEffect(GamePhase.RedrawCardsPhase)))
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
    numberInput.removeEventHandler(MouseEvent.MOUSE_CLICKED, spinnerEventHandler)
    okButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, okButtonEventHandler)
    cancelButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, cancelButtonEventHandler)
    discardButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, discardButtonEventHandler)
    drawButton.removeEventHandler(MouseEvent.MOUSE_CLICKED, drawButtonEventHandler)

  override protected def getPane: Node = deckPane

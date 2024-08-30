package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.{
  CancelDiscardMessage,
  ConfirmDiscardMessage,
  DrawCardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewSubject,
  ViewSubject
}
import it.unibo.view.GUIType
import it.unibo.view.components.ISidebarComponent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Button, Spinner, SpinnerValueFactory}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

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

  @FXML
  private def initialize(): Unit =
    updateSpinnerValueFactory()
    setDiscardProcedureButtonsEnabled(false)
    handlers.foreach(h => deckPane.addEventHandler(MouseEvent.MOUSE_CLICKED, h))

  private def updateSpinnerValueFactory(): Unit =
    val valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCards, 1)
    numberInput.setValueFactory(valueFactory)
    numberInput.getValueFactory.setValue(1)

  private def setDiscardProcedureButtonsEnabled(enabled: Boolean): Unit =
    okButton.setDisable(!enabled)
    cancelButton.setDisable(!enabled)
    discardButton.setDisable(enabled)

  private def handleDrawCard(): Unit = observable.onNext(DrawCardMessage(numberInput.getValue))

  private def startDiscardProcedure(): Unit =
    internalObservable.onNext(InitializeDiscardProcedureMessage())
    setDiscardProcedureButtonsEnabled(true)

  private def discard(): Unit =
    internalObservable.onNext(ConfirmDiscardMessage())
    setDiscardProcedureButtonsEnabled(false)

  private def cancel(): Unit =
    internalObservable.onNext(CancelDiscardMessage())
    setDiscardProcedureButtonsEnabled(false)

  override protected def onEnableView(): Unit =
    super.onEnableView()
    handlers.foreach(h => deckPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, h))

  override protected def onDisableView(): Unit =
    super.onDisableView()
    handlers.foreach(h => deckPane.addEventHandler(MouseEvent.MOUSE_CLICKED, h))

  override protected def getPane: Node = deckPane

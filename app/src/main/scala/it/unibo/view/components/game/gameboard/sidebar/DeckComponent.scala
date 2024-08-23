package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.{
  CancelDiscardMessage,
  ConfirmDiscardMessage,
  DrawCardMessage,
  InitializeDiscardProcedureMessage,
  InternalViewSubject,
  ViewSubject
}
import it.unibo.view.{GUIType, GameBoardController}
import it.unibo.view.components.IHaveView
import javafx.fxml.FXML
import javafx.scene.control.{Button, Spinner, SpinnerValueFactory}

import scala.compiletime.uninitialized

final class DeckComponent(using observable: ViewSubject, internalObservable: InternalViewSubject)
    extends IHaveView:

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

  @FXML
  private def initialize(): Unit =
    updateSpinnerValueFactory()
    setDiscardProcedureButtonsEnabled(false)

  private def updateSpinnerValueFactory(): Unit =
    val valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCards, 1)
    numberInput.setValueFactory(valueFactory)
    numberInput.getValueFactory.setValue(1)

  private def setDiscardProcedureButtonsEnabled(enabled: Boolean): Unit =
    okButton.setDisable(!enabled)
    cancelButton.setDisable(!enabled)
    discardButton.setDisable(enabled)

  @FXML
  private def handleDrawCard(): Unit = observable.onNext(DrawCardMessage(numberInput.getValue))

  @FXML
  private def startDiscardProcedure(): Unit =
    internalObservable.onNext(InitializeDiscardProcedureMessage())
    setDiscardProcedureButtonsEnabled(true)

  @FXML
  private def discard(): Unit =
    internalObservable.onNext(ConfirmDiscardMessage())
    setDiscardProcedureButtonsEnabled(false)

  @FXML
  private def cancel(): Unit =
    internalObservable.onNext(CancelDiscardMessage())
    setDiscardProcedureButtonsEnabled(false)

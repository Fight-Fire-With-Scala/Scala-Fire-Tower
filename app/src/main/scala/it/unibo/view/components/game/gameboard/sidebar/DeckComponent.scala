package it.unibo.view.components.game.gameboard.sidebar

import it.unibo.controller.{DrawCardMessage, ViewSubject}
import it.unibo.view.GUIType
import it.unibo.view.components.IHaveView
import javafx.fxml.FXML
import javafx.scene.control.{Spinner, SpinnerValueFactory}

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class DeckComponent(using observable: ViewSubject) extends IHaveView:

  @FXML
  private var numberInput: Spinner[Integer] = uninitialized

  private var maxCards: Int = 5

  override val fxmlPath: String = GUIType.Deck.fxmlPath

  def maxCards_=(value: Int): Unit =
    maxCards = value
    updateSpinnerValueFactory()

  @FXML
  private def initialize(): Unit = updateSpinnerValueFactory()

  private def updateSpinnerValueFactory(): Unit =
    val valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCards, 1)
    numberInput.setValueFactory(valueFactory)
    numberInput.getValueFactory.setValue(1)

  @FXML
  private def handleDrawCard(): Unit = observable.onNext(DrawCardMessage(numberInput.getValue))

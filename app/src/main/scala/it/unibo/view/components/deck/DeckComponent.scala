package it.unibo.view.components.deck

import it.unibo.controller.{DrawCardMessage, ViewSubject}
import it.unibo.view.components.GraphicComponent
import javafx.fxml.FXML
import javafx.scene.control.{Spinner, SpinnerValueFactory}

import scala.compiletime.uninitialized

class DeckComponent(observableSubject: ViewSubject) extends GraphicComponent:

  @FXML
  private var numberInput: Spinner[Integer] = uninitialized

  private var maxCards: Int = 5

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
  private def handleDrawCard(): Unit = observableSubject
    .onNext(DrawCardMessage(numberInput.getValue))

package it.unibo.view.controllers.deck

import it.unibo.controller.ViewSubject
import it.unibo.view.controllers.GraphicController
import javafx.fxml.FXML
import javafx.scene.control.{Spinner, SpinnerValueFactory}

import scala.compiletime.uninitialized

class DeckController(observableSubject: ViewSubject) extends GraphicController:

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
  private def handleDrawCard(): Unit =
    val number = numberInput.getValue
    println(s"Number selected: $number")
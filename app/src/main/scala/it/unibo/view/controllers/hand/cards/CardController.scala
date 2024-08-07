package it.unibo.view.controllers.hand.cards

import it.unibo.model.cards.{Card, CardType}
import it.unibo.view.controllers.GraphicController
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import javafx.scene.input.MouseEvent
import scalafx.Includes._

import scala.compiletime.uninitialized

class CardController extends GraphicController:
  @FXML
  var cardPane: Pane = uninitialized
  @FXML
  var cardTitle: Text = uninitialized
  @FXML
  var cardDescription: Text = uninitialized
  @FXML
  var cardId: String = uninitialized

  def setCard(card: Option[Card]): Unit = card match
    case Some(card) =>
      cardPane.getStyleClass.clear()
      cardPane.getStyleClass.add("card")
      cardPane.getStyleClass.add(getStyleClassForCardType(card.cardType))
      cardTitle.setText(card.cardType.title)
      cardDescription.setText(card.cardType.description)
      cardId = card.id.toString
      cardPane
        .addEventHandler(MouseEvent.MOUSE_CLICKED, (_: MouseEvent) => println(s"Card ID: $cardId"))
    case None       =>
      cardPane.getStyleClass.clear()
      cardPane.getStyleClass.add("default")
      cardTitle.setText("")
      cardDescription.setText("")
      cardId = ""

  private def getStyleClassForCardType(cardType: CardType): String =
    cardType.effectType.id match
      case 0 | 1 | 2 | 3     => "fire"
      case 4 | 5 | 6 | 7     => "wind"
      case 8 | 9 | 10        => "firebreak"
      case 11 | 12 | 13 | 14 => "water"
      case _                 => "default"

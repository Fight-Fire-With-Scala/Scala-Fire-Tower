package it.unibo.view.components.hand.cards

import it.unibo.model.cards.Card.allCards
import it.unibo.model.cards.types.{FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.cards.{Card, CardType}
import it.unibo.view.components.GraphicComponent
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import javafx.scene.input.MouseEvent
import scalafx.Includes.*

import scala.compiletime.uninitialized

class CardComponent extends GraphicComponent:
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
    allCards.find(_.id == cardType.effectType.id) match
      case Some(_: FireCard)      => "fire"
      case Some(_: WindCard)      => "wind"
      case Some(_: FirebreakCard) => "firebreak"
      case Some(_: WaterCard)     => "water"
      case _                      => "default"

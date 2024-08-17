package it.unibo.view.components.game.gameboard.hand

import it.unibo.model.cards.Card.allCards
import it.unibo.model.cards.types.{FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.cards.{Card, CardType}
import it.unibo.view.components.GraphicComponent
import it.unibo.view.components.IHaveView
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import scalafx.Includes.*

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class CardComponent extends GraphicComponent with IHaveView:

  override val fxmlPath: String = "/pages/card.fxml"

  @FXML
  var cardPane: Pane = uninitialized
  @FXML
  private var cardTitle: Text = uninitialized
  @FXML
  private var cardDescription: Text = uninitialized
  @FXML
  var cardId: String = uninitialized

  def setCard(card: Option[Card]): Unit = card match
    case Some(card) =>
      cardPane.getStyleClass.clear()
      cardPane.getStyleClass.add("card")
      cardPane.getStyleClass.add(getStyleClassForCardType(card.cardType))
      cardTitle.setText(card.cardType.title)
      cardDescription.setText(card.cardType.description)
      cardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, cardClickHandler)
      cardId = card.id.toString
      cardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, cardClickHandler)
    case None       =>
      cardPane.getStyleClass.clear()
      cardPane.getStyleClass.add("card")
      cardPane.getStyleClass.add("default")
      cardTitle.setText("")
      cardDescription.setText("")
      cardId = ""
      cardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, cardClickHandler)

  private def getStyleClassForCardType(cardType: CardType): String =
    allCards.find(_.id == cardType.effectType.id) match
      case Some(_: FireCard)      => "fire"
      case Some(_: WindCard)      => "wind"
      case Some(_: FirebreakCard) => "firebreak"
      case Some(_: WaterCard)     => "water"
      case _                      => "not-found"

  private val cardClickHandler: EventHandler[MouseEvent] =
    (_: MouseEvent) => println(s"Card ID: $cardId")

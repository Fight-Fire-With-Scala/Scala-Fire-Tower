package it.unibo.view.components.game.gameboard.hand

import it.unibo.controller.{CandidateCardToPlayMessage, InternalViewSubject, ToggleCardInListMessage, UpdateGamePhaseView}
import it.unibo.model.cards.Card.allCards
import it.unibo.model.cards.types.{CanBeDiscarded, FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.cards.{Card, CardType}
import it.unibo.model.gameboard.GamePhase.PlayCard
import it.unibo.view.GUIType
import it.unibo.view.components.{ICanBeDisabled, IHandComponent, IHaveView}
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import scalafx.Includes.*

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class CardComponent(using internalObservable: InternalViewSubject) extends IHandComponent:

  override val fxmlPath: String = GUIType.Card.fxmlPath

  @FXML
  private var cardPane: Pane = uninitialized
  @FXML
  private var cardTitle: Text = uninitialized
  @FXML
  private var cardDescription: Text = uninitialized
  @FXML
  var cardId: String = uninitialized
  @FXML
  var discardable: Boolean = uninitialized

  private val playCardHandler: EventHandler[MouseEvent] = (_: MouseEvent) =>
    internalObservable.onNext(CandidateCardToPlayMessage(cardId.toInt))
    internalObservable.onNext(UpdateGamePhaseView(PlayCard))

  private val discardCardHandler: EventHandler[MouseEvent] = (_: MouseEvent) =>
    internalObservable.onNext(ToggleCardInListMessage(cardId.toInt))
    toggleHighlight()

  private var activeEventHandler: EventHandler[MouseEvent] = playCardHandler

  def setCard(card: Card): Unit =
    cardPane.getStyleClass.clear()
    cardPane.getStyleClass.add("card")
    cardPane.getStyleClass.add(getStyleClassForCardType(card.cardType))
    cardTitle.setText(card.cardType.title)
    cardDescription.setText(card.cardType.description)
    cardId = card.id.toString
    cardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, activeEventHandler)
    card.cardType.effectType match
      case _: CanBeDiscarded => discardable = true
      case _                 => discardable = false

  private def getStyleClassForCardType(cardType: CardType): String =
    allCards.find(_.id == cardType.effectType.id) match
      case Some(_: FireCard)      => "fire"
      case Some(_: WindCard)      => "wind"
      case Some(_: FirebreakCard) => "firebreak"
      case Some(_: WaterCard)     => "water"
      case _                      => "not-found"

  def reset(): Unit =
    cardPane.getStyleClass.clear()
    cardTitle.setText("")
    cardDescription.setText("")
    cardId = ""
    discardable = false
    cardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, activeEventHandler)
    activeEventHandler = playCardHandler

  def swapHandler(toDiscard: Boolean): Unit =
    cardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, activeEventHandler)
    activeEventHandler =
      if (toDiscard) discardCardHandler
      else {
        cardPane.getStyleClass.remove("highlight")
        playCardHandler
      }

    cardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, activeEventHandler)

  def toggleHighlight(): Unit =
    if cardPane.getStyleClass.contains("highlight") then cardPane.getStyleClass.remove("highlight")
    else cardPane.getStyleClass.add("highlight")

  override def onEnableView(): Unit =
    super.onEnableView()
    cardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, activeEventHandler)
  
  override def onDisableView(): Unit =
    super.onDisableView()
    cardPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, activeEventHandler)

  override protected def getPane: Node = cardPane
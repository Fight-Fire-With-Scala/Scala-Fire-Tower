package it.unibo.view.components.game.gameboard.hand

import it.unibo.controller.{
  CandidateCardToPlayMessage,
  InternalViewSubject,
  ToggleCardInListMessage
}
import it.unibo.model.cards.Card.allCards
import it.unibo.model.cards.types.{CanBeDiscarded, FireCard, FirebreakCard, WaterCard, WindCard}
import it.unibo.model.cards.{Card, CardType}
import it.unibo.model.gameboard.GamePhase
import it.unibo.view.GUIType
import it.unibo.view.components.game.gameboard.hand.CardHighlightState.Unhighlighted
import it.unibo.view.components.{ICanBeDisabled, ICanToggleHandler, IHandComponent}
import javafx.event.{EventHandler, EventType}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import scalafx.Includes.*

import scala.compiletime.uninitialized

enum CardHighlightState:
  case Highlighted, Unhighlighted

final class CardComponent(using internalObservable: InternalViewSubject)
    extends IHandComponent, ICanToggleHandler[GamePhase], ICanBeDisabled:

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
  var containSpecialCard: Boolean = uninitialized

  @FXML
  def initialize(): Unit = highlightManager.initialize(cardPane)

  var highlightManager = CardHighlightManager()

  protected var currentState: GamePhase = GamePhase.PlayStandardCardPhase

  private val playCardHandler: EventHandler[MouseEvent] =
    (_: MouseEvent) => internalObservable.onNext(CandidateCardToPlayMessage(cardId.toInt))

  private val discardCardHandler: EventHandler[MouseEvent] = (_: MouseEvent) =>
    internalObservable.onNext(ToggleCardInListMessage(cardId.toInt))
    highlightManager.toggle()

  addHandler(GamePhase.PlayStandardCardPhase, MouseEvent.MOUSE_CLICKED, playCardHandler)
  addHandler(GamePhase.WaitingPhase, MouseEvent.MOUSE_CLICKED, playCardHandler)
  addHandler(GamePhase.RedrawCardsPhase, MouseEvent.MOUSE_CLICKED, discardCardHandler)

  protected def applyState(state: GamePhase): Unit = highlightManager.toggle(Some(Unhighlighted))

  def setCard(card: Card): Unit =
    cardPane.getStyleClass.clear()
    cardPane.getStyleClass.add("card")
    cardPane.getStyleClass.add(getStyleClassForCardType(card.cardType))
    cardTitle.setText(card.cardType.title)
    cardDescription.setText(card.cardType.description)
    cardId = card.id.toString
    card.cardType.effectType match
      case _: CanBeDiscarded => containSpecialCard = true
      case _                 => containSpecialCard = false

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
    containSpecialCard = false
    disableActualHandlers()

  override def onEnableView(): Unit =
    super.onEnableView()
    enableActualHandlers()

  override def onDisableView(): Unit =
    super.onDisableView()
    disableActualHandlers()

  override protected def getPane: Node = cardPane

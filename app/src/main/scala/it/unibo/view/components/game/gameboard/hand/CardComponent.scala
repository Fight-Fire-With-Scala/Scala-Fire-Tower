package it.unibo.view.components.game.gameboard.hand

import scala.compiletime.uninitialized

import it.unibo.controller.CandidateCardToPlayMessage
import it.unibo.controller.InternalViewSubject
import it.unibo.controller.ToggleCardInListMessage
import it.unibo.model.cards.Card
import it.unibo.model.effects.cards.BucketEffect
import it.unibo.model.effects.cards.FireEffect
import it.unibo.model.effects.cards.FirebreakEffect
import it.unibo.model.effects.cards.WaterEffect
import it.unibo.model.effects.cards.WindEffect
import it.unibo.model.effects.core.CanBeDiscarded
import it.unibo.model.effects.core.ICardEffect
import it.unibo.model.effects.core.ISpecialCardEffect
import it.unibo.model.effects.core.IStandardCardEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.view.GUIType
import it.unibo.view.components.ICanBeDisabled
import it.unibo.view.components.ICanSwitchHandler
import it.unibo.view.components.IHandComponent
import it.unibo.view.components.game.gameboard.hand.CardHighlightState.Unhighlighted
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import scalafx.Includes._

enum CardHighlightState:
  case Highlighted, Unhighlighted

final class CardComponent(using internalObservable: InternalViewSubject)
    extends IHandComponent, ICanSwitchHandler[GamePhase], ICanBeDisabled:

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
    highlightManager.switch()

  private def addHandlers(): Unit =
    if containSpecialCard then
      addHandler(GamePhase.PlaySpecialCardPhase, MouseEvent.MOUSE_CLICKED, playCardHandler)
    else
      addHandler(GamePhase.PlayStandardCardPhase, MouseEvent.MOUSE_CLICKED, playCardHandler)
      addHandler(GamePhase.WaitingPhase, MouseEvent.MOUSE_CLICKED, playCardHandler)
      addHandler(GamePhase.RedrawCardsPhase, MouseEvent.MOUSE_CLICKED, discardCardHandler)

  protected def applyState(state: GamePhase): Unit = highlightManager.switch(Some(Unhighlighted))

  def setCard(card: Card): Unit =
    cardPane.getStyleClass.clear()
    cardPane.getStyleClass.add("card")
    cardPane.getStyleClass.add(getStyleClassForCardType(card.effect))
    cardTitle.setText(card.title)
    cardDescription.setText(card.description)
    cardId = card.id.toString
    card.effect match
      case _: CanBeDiscarded => containSpecialCard = false
      case _                 => containSpecialCard = true

    addHandlers()

  private def getStyleClassForCardType(effect: ICardEffect): String = effect match
    case effect: IStandardCardEffect => effect match
        case effect: FireEffect      => "fire"
        case effect: FirebreakEffect => "firebreak"
        case effect: WaterEffect     => "water"
        case effect: WindEffect      => "wind"
        case _                       => "not-found"
    case effect: ISpecialCardEffect  => effect match
        case BucketEffect => "water"
        case _            => "not-found"

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

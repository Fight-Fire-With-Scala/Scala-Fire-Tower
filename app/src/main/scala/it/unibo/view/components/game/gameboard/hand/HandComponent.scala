package it.unibo.view.components.game.gameboard.hand

import it.unibo.controller.{
  DiscardCardMessage,
  DrawCardMessage,
  InternalViewSubject,
  ResetPatternComputation,
  ResolvePatternComputation,
  UpdateGamePhaseModel,
  UpdateGamePhaseView,
  ViewSubject
}
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.{
  DecisionPhase,
  EndTurnPhase,
  PlaySpecialCardPhase,
  PlayStandardCardPhase,
  WaitingPhase
}
import it.unibo.view.{logger, GUIType}
import it.unibo.view.components.{IHandComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

final class HandComponent(val cardComponents: List[CardComponent])(using
    observable: ViewSubject,
    internalObservable: InternalViewSubject
) extends IHandComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Hand.fxmlPath

  private var cardToPlay: Option[CardComponent] = None

  private var cardToRemove = List.empty[Int]

  @FXML
  private var handPane: Pane = uninitialized

  private val addComponent =
    (component: CardComponent) => handPane.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit =
    cardComponents.foreach(addComponent)
    enableView()

  def updateHand(cards: List[it.unibo.model.cards.Card])(gamePhase: GamePhase): Unit =
    runOnUIThread {
      cardComponents.foreach(_.reset())
      cardComponents.zip(cards).foreach { case (cardComponent, card) =>
        cardComponent.setCard(card)
        cardComponent.toggle(gamePhase)
      }
      cardToPlay.foreach(_.highlightManager.toggle(Some(CardHighlightState.Highlighted)))

    }

  def initDiscardProcedure(): Unit = cardComponents.foreach(cardComponent => cardToPlay = None)

  def cancelDiscardProcedure(): Unit =
    observable.onNext(UpdateGamePhaseModel(WaitingPhase))
    endDiscardProcedure()

  private def endDiscardProcedure(): Unit = cardToRemove = List.empty

  def toggleCardInDiscardList(cardId: Int): Unit = cardToRemove =
    if cardToRemove.contains(cardId) then cardToRemove.filterNot(_ == cardId)
    else cardId :: cardToRemove

  def discardCards(): Unit =
    observable.onNext(DiscardCardMessage(cardToRemove))
    observable.onNext(DrawCardMessage(cardToRemove.size))
    observable.onNext(UpdateGamePhaseModel(DecisionPhase))
    endDiscardProcedure()

  def confirmCardPlay(): Unit =
    cardToPlay.foreach(_.highlightManager.toggle(Some(CardHighlightState.Unhighlighted)))
    cardToPlay = None

  def cardToPlay_=(cardId: Int): Unit =
    val cardComponent = cardComponents.find(_.cardId == cardId.toString)
    cardComponent.get.highlightManager.toggle()
    if cardToPlay == cardComponent then
      cardToPlay = None
      logger.info(s"Card not to play: $cardToPlay")
      observable.onNext(ResetPatternComputation())
      observable.onNext(UpdateGamePhaseModel(WaitingPhase))
      internalObservable.onNext(UpdateGamePhaseView(WaitingPhase))
    else
      cardToPlay match
        case Some(component) => component.highlightManager
            .toggle(Some(CardHighlightState.Unhighlighted))
        case None            =>
      cardToPlay = cardComponent
      logger.info(s"Card to play: $cardToPlay")
      observable.onNext(ResolvePatternComputation(cardId))
      observable.onNext(UpdateGamePhaseModel(PlayStandardCardPhase))
      internalObservable.onNext(UpdateGamePhaseView(PlayStandardCardPhase))

  override def onEnableView(): Unit =
    super.onEnableView()
    cardComponents.foreach(card => card.enableView())

  override def onDisableView(): Unit =
    super.onDisableView()
    cardComponents.foreach(card => card.disableView())

  def handleSpecialCardComponents(gamePhase: GamePhase): Unit = gamePhase match
    case PlaySpecialCardPhase => cardComponents.find(_.containSpecialCard).foreach(_.enableView())
    case _                    => cardComponents.foreach(_.disableView())

  override protected def getPane: Node = handPane

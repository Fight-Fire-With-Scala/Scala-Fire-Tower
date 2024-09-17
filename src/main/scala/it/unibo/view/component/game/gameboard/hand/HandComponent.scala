package it.unibo.view.component.game.gameboard.hand

import scala.compiletime.uninitialized

import it.unibo.controller.ChoseCardToPlay
import it.unibo.controller.DiscardCardMessage
import it.unibo.controller.DrawCardMessage
import it.unibo.controller.ResolveCardReset
import it.unibo.controller.UpdateGamePhase
import it.unibo.controller.ViewSubject
import it.unibo.model.effect.hand.HandEffect.DiscardCard
import it.unibo.model.effect.hand.HandEffect.DrawCard
import it.unibo.model.effect.hand.HandEffect.PlayCard
import it.unibo.model.effect.phase.PhaseEffect
import it.unibo.model.gameboard.GamePhase
import it.unibo.model.gameboard.GamePhase.DecisionPhase
import it.unibo.model.gameboard.GamePhase.PlaySpecialCardPhase
import it.unibo.model.gameboard.GamePhase.PlayStandardCardPhase
import it.unibo.model.gameboard.GamePhase.WaitingPhase
import it.unibo.view.GUIType
import it.unibo.view.component.IHandComponent
import it.unibo.view.component.IUpdateView
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane

final class HandComponent(val cardComponents: List[CardComponent])(using observable: ViewSubject)
    extends IHandComponent
    with IUpdateView:

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

  def updateHand(cards: List[it.unibo.model.card.Card])(gamePhase: GamePhase): Unit =
    runOnUIThread:
      cardComponents.foreach(_.reset())
      cardComponents.zip(cards).foreach { case (cardComponent, card) =>
        cardComponent.setCard(card)
        cardComponent.switch(gamePhase)
      }
      cardToPlay.foreach(_.highlightManager.switch(Some(CardHighlightState.Highlighted)))

  def initDiscardProcedure(): Unit = cardComponents.foreach(cardComponent => cardToPlay = None)

  def cancelDiscardProcedure(): Unit =
    observable.onNext(UpdateGamePhase(PhaseEffect(WaitingPhase)))
    endDiscardProcedure()

  private def endDiscardProcedure(): Unit = cardToRemove = List.empty

  def toggleCardInDiscardList(cardId: Int): Unit = cardToRemove =
    if cardToRemove.contains(cardId) then cardToRemove.filterNot(_ == cardId)
    else cardId :: cardToRemove

  def discardCards(): Unit =
    observable.onNext(DiscardCardMessage(DiscardCard(cardToRemove)))
    observable.onNext(DrawCardMessage(DrawCard(cardToRemove.size)))
    observable.onNext(UpdateGamePhase(PhaseEffect(DecisionPhase)))
    endDiscardProcedure()

  def confirmCardPlay(): Unit =
    cardToPlay.foreach(_.highlightManager.switch(Some(CardHighlightState.Unhighlighted)))
    cardToPlay = None

  def cardToPlay_=(cardId: Int): Unit =
    val cardComponent = cardComponents.find(_.cardId == cardId.toString)
    cardComponent.get.highlightManager.switch()
    if cardToPlay == cardComponent then
      cardToPlay = None
      observable.onNext(ResolveCardReset())
      if !cardComponent.get.containSpecialCard then
        observable.onNext(UpdateGamePhase(PhaseEffect(WaitingPhase)))
    else
      cardToPlay match
        case Some(component) =>
          component.highlightManager
            .switch(Some(CardHighlightState.Unhighlighted))
        case None =>
      cardToPlay = cardComponent
      observable.onNext(ChoseCardToPlay(PlayCard(cardId)))
      if !cardComponent.get.containSpecialCard then
        observable.onNext(UpdateGamePhase(PhaseEffect(PlayStandardCardPhase)))

  override def onEnableView(): Unit =
    super.onEnableView()
    cardComponents.foreach(card => card.enableView())

  override def onDisableView(): Unit =
    super.onDisableView()
    cardComponents.foreach(card => card.disableView())

  def handleSpecialCardComponents(gamePhase: GamePhase): Unit =
    val (specialCardComponents, normalCardComponents) =
      cardComponents.partition(_.containSpecialCard)
    gamePhase match
      case PlaySpecialCardPhase =>
        specialCardComponents.foreach(_.enableView())
        normalCardComponents.foreach(_.disableView())
      case PlayStandardCardPhase =>
        specialCardComponents.foreach(_.disableView())
        normalCardComponents.foreach(_.enableView())
      case _ =>
        specialCardComponents.foreach(_.disableView())
        normalCardComponents.foreach(_.disableView())

  override protected def getPane: Node = handPane

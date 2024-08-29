package it.unibo.view.components.game.gameboard.hand

import it.unibo.controller.{
  DiscardTheseCardsMessage,
  DrawCardMessage,
  ResetPatternComputation,
  ResolvePatternComputation,
  ViewSubject
}
import it.unibo.view.GUIType
import it.unibo.view.components.{IMainComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class HandComponent(val cardComponents: List[CardComponent])(using observable: ViewSubject)
    extends IMainComponent with IUpdateView:

  override val fxmlPath: String = GUIType.Hand.fxmlPath

  private var cardToPlay: Option[CardComponent] = None

  private var cardToRemove = List.empty[Int]

  @FXML
  private var handPane: Pane = uninitialized

  private val addComponent =
    (component: CardComponent) => handPane.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit = cardComponents.foreach(addComponent)

  def updateHand(cards: List[it.unibo.model.cards.Card]): Unit = runOnUIThread {
    cardComponents.foreach(_.reset())
    cardComponents.zip(cards).foreach { case (cardComponent, card) => cardComponent.setCard(card) }
  }

  def initDiscardProcedure(): Unit = cardComponents.foreach { cardComponent =>
    if cardComponent.discardable then cardComponent.swapHandler(true)
  }

  def endDiscardProcedure(): Unit =
    cardComponents.foreach(_.swapHandler(false))
    cardToRemove = List.empty

  def toggleCardInDiscardList(cardId: Int): Unit = cardToRemove =
    if cardToRemove.contains(cardId) then cardToRemove.filterNot(_ == cardId)
    else cardId :: cardToRemove

  def discardCards(): Unit =
    observable.onNext(DiscardTheseCardsMessage(cardToRemove))
    observable.onNext(DrawCardMessage(cardToRemove.size))
    endDiscardProcedure()

  def cardToPlay_=(cardId: Int): Unit =
    val cardComponent = cardComponents.find(_.cardId == cardId.toString)
    cardComponent.get.toggleHighlight()
    if cardToPlay == cardComponent then
      cardToPlay = None
      observable.onNext(ResetPatternComputation())
    else
      cardToPlay match
        case None    => observable.onNext(ResolvePatternComputation(cardId))
        case Some(_) =>
          observable.onNext(ResetPatternComputation())
          observable.onNext(ResolvePatternComputation(cardId))
          cardToPlay.get.toggleHighlight()
      cardToPlay = cardComponent

  override def generalToggle(): Unit = super.generalToggle()
//    cardComponents.foreach(card => card.generalToggle())

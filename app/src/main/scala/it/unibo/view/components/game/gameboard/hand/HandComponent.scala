package it.unibo.view.components.game.gameboard.hand

import it.unibo.controller.{DiscardTheseCardsMessage, DrawCardMessage, ResetPatternComputation, UpdateGamePhaseModel, ViewSubject}
import it.unibo.model.gameboard.GamePhase.PlayCard
import it.unibo.view.GUIType
import it.unibo.view.components.{IHandComponent, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.layout.Pane

import scala.compiletime.uninitialized

//noinspection VarCouldBeVal
final class HandComponent(val cardComponents: List[CardComponent])(using observable: ViewSubject)
    extends IHandComponent with IUpdateView:

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
        case None    =>
//          observable.onNext(ResolvePatternComputation(cardId))
          observable.onNext(UpdateGamePhaseModel(PlayCard))
        case Some(_) =>
          observable.onNext(ResetPatternComputation())
          observable.onNext(UpdateGamePhaseModel(PlayCard))
//          observable.onNext(ResolvePatternComputation(cardId))
          cardToPlay.get.toggleHighlight()
      cardToPlay = cardComponent
    
  override def onEnableView(): Unit =
    super.onEnableView()
    cardComponents.foreach(card => card.enableView())

  override def onDisableView(): Unit =
    super.onDisableView()
    cardComponents.foreach(card => card.disableView())

  override protected def getPane: Node = handPane

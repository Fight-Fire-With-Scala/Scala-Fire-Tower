package it.unibo.view.components.game.gameboard.hand

import it.unibo.view.GUIType
import it.unibo.view.components.{GraphicComponent, IHaveView, IUpdateView}
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import scalafx.application.Platform

import scala.compiletime.uninitialized

final class HandComponent(val cardComponents: List[CardComponent]) extends GraphicComponent with IHaveView with IUpdateView:

  override val fxmlPath: String = GUIType.Hand.fxmlPath

  @FXML
  var handPane: Pane = uninitialized

  private val addComponent =
    (component: CardComponent) => handPane.getChildren.add(component.getView)

  @FXML
  def initialize(): Unit = cardComponents.foreach(addComponent)

  def updateHand(cards: List[it.unibo.model.cards.Card]): Unit = Platform.runLater { () =>
    cardComponents.zipWithIndex.foreach { case (cardComponent, index) =>
      val card = cards.lift(index)
      cardComponent.setCard(card)
    }
  }

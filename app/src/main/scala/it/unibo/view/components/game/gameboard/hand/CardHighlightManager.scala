package it.unibo.view.components.game.gameboard.hand

import it.unibo.view.components.Switchable
import javafx.scene.layout.Pane

final class CardHighlightManager extends Switchable[Option[CardHighlightState]]:
  var cardPane: Option[Pane] = None
  def initialize(cp: Pane): Unit = cardPane = Some(cp)

  private var highlightState: CardHighlightState = CardHighlightState.Unhighlighted

  override def switch(toState: Option[CardHighlightState] = None): Unit =
    highlightState = toState.getOrElse(
      if highlightState == CardHighlightState.Highlighted then CardHighlightState.Unhighlighted
      else CardHighlightState.Highlighted
    )
    highlightState match
      case CardHighlightState.Highlighted   => cardPane.foreach(_.getStyleClass.add("highlight"))
      case CardHighlightState.Unhighlighted => cardPane.foreach(_.getStyleClass.remove("highlight"))

  override def getCurrentState: Option[CardHighlightState] = Some(highlightState)

package it.unibo.model.cards.effects

import it.unibo.model.gameboard.grid.{Position, Token}

case class PatternComputationEffect(patterns: List[Map[Position, Token]]) extends CardEffect

case class PatternChoiceEffect(pattern: Map[Position, Token]) extends CardEffect
package it.unibo.model.gameboard

enum GamePhase:
  case WindPhase, RedrawCardsPhase, PlayStandardCardPhase, WaitingPhase, PlaySpecialCardPhase,
    EndTurnPhase, DecisionPhase

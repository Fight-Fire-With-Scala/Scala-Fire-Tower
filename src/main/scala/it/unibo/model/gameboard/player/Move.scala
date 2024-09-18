package it.unibo.model.gameboard.player

import it.unibo.model.effect.MoveEffect

final case class Move(turnNumber: Int, effect: MoveEffect)

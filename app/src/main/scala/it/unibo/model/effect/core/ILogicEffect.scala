package it.unibo.model.effect.core

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.grid.Position
import it.unibo.model.gameboard.grid.Token
import it.unibo.model.prolog.Rule

final case class ILogicEffect(
    pattern: Map[Position, Token],
    goals: List[Int => Rule],
    directions: List[Direction]
) extends IGameEffect

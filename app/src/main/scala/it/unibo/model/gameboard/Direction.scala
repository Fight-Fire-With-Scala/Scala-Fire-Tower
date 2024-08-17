package it.unibo.model.gameboard

import it.unibo.model.gameboard.grid.Position

enum Direction(id: String, delta: Position):
  case North extends Direction("north", Position(-1, 0))
  case South extends Direction("south", Position(1, 0))
  case East extends Direction("east", Position(0, 1))
  case West extends Direction("west", Position(0, -1))

  def getId: String = id
  def getDelta: Position = delta

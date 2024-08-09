package it.unibo.model.gameboard.grid

enum Cell(val id: String):
  case EternalFire extends Cell("ef")
  case Woods extends Cell("w")
  case Tower extends Cell("t")

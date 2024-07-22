package it.unibo.model.gameboard

import scala.util.Random

class Dice[T](values: Seq[T], seed: Long) {
  private val rand: util.Random = Random(seed)
  private val high = values.size + 1
  private val low = 1
  def roll(): T = values((rand.nextInt(high - low) + low) - 1)
}
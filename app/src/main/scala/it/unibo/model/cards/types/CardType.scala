package it.unibo.model.cards.types

import it.unibo.model.cards._

trait HasEffect:
  def effectCode: Int

trait HasSpatialEffect[T] extends HasEffect:
  def effect: () => T
  def spatialPattern: Matrix[PatternCell]

trait HasMultipleEffects[C, T] extends HasEffect:
  def choices: Set[C]
  def effect: (choice: C) => T
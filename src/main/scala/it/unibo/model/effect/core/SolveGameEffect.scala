package it.unibo.model.effect.core

trait SolveGameEffect[InputEffect <: GameEffect, OutputEffect <: GameEffect] extends GameEffect:
  protected val solver: InputEffect => OutputEffect
  def solve(effect: InputEffect): OutputEffect = solver(effect)

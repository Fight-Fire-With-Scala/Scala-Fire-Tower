package it.unibo.model.effect.core

trait ISolveGameEffect[InputEffect <: IGameEffect, OutputEffect <: IGameEffect] extends IGameEffect:
  protected val solver: InputEffect => OutputEffect
  def solve(effect: InputEffect): OutputEffect = solver(effect)

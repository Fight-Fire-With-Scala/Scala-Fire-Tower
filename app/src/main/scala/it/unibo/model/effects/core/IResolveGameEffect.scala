package it.unibo.model.effects.core

trait IResolveGameEffect[InputEffect <: IGameEffect, OutputEffect <: IGameEffect] extends IGameEffect:
  protected val resolver: InputEffect => OutputEffect
  def resolve(effect: InputEffect): OutputEffect = resolver(effect)

package it.unibo.model.effect.core

trait IResolveGameEffect[InputEffect <: IGameEffect, OutputEffect <: IGameEffect] extends IGameEffect:
  protected val resolver: InputEffect => OutputEffect
  def resolve(effect: InputEffect): OutputEffect = resolver(effect)

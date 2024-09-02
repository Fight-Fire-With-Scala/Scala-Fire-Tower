package it.unibo.view.components

import it.unibo.view.logger
import javafx.event.{EventHandler, EventType}
import javafx.scene.input.MouseEvent

trait Toggleable[T]:
  def toggle(toState: T): Unit
  def getCurrentState: T

trait ICanToggleHandler[T] extends Toggleable[T]:

  protected var currentState: T
  protected var stateHandlers: Map[T, Map[EventType[MouseEvent], List[EventHandler[MouseEvent]]]] =
    Map()

  protected def applyState(state: T): Unit

  protected def onToggle(state: T): Unit

  def addHandler(
      state: T,
      eventType: EventType[MouseEvent],
      handler: EventHandler[MouseEvent]
  ): Unit =
    val eventHandlers = stateHandlers.getOrElse(state, Map())
    val handlers = eventHandlers.getOrElse(eventType, List())
    stateHandlers += (state -> (eventHandlers + (eventType -> (handler :: handlers))))

  def removeHandler(
      state: T,
      eventType: EventType[MouseEvent],
      handler: EventHandler[MouseEvent]
  ): Unit =
    val eventHandlers = stateHandlers.getOrElse(state, Map())
    val handlers = eventHandlers.getOrElse(eventType, List()).filterNot(_ == handler)
    stateHandlers += (state -> (eventHandlers + (eventType -> handlers)))

  def getHandlers(state: T, eventType: EventType[MouseEvent]): List[EventHandler[MouseEvent]] =
    stateHandlers.getOrElse(state, Map()).getOrElse(eventType, List())

  override def toggle(toState: T): Unit =
    currentState = toState
    //logger.info(s"Current state $currentState")
    applyState(toState)
    onToggle(toState)

  override def getCurrentState: T = currentState

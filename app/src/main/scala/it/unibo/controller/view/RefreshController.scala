package it.unibo.controller.view

import it.unibo.controller.logger
import it.unibo.controller.view.RefreshType.CardDeselected
import it.unibo.controller.view.RefreshType.CardDiscard
import it.unibo.controller.view.RefreshType.CardDraw
import it.unibo.controller.view.RefreshType.CardSelected
import it.unibo.controller.view.RefreshType.PatternChosen
import it.unibo.controller.view.RefreshType.PhaseUpdate
import it.unibo.controller.view.RefreshType.WindUpdate
import it.unibo.model.effect.MoveEffect
import it.unibo.model.effect.MoveEffect.CardChosen
import it.unibo.model.effect.MoveEffect.PatternChosen
import it.unibo.model.gameboard.GameBoard
import it.unibo.model.gameboard.GamePhase.WindPhase
import it.unibo.model.gameboard.player.Bot
import it.unibo.model.gameboard.player.Move
import it.unibo.model.gameboard.player.Person
import it.unibo.model.gameboard.player.Player
import it.unibo.view.component.game.GameComponent
import it.unibo.view.component.game.gameboard.sidebar.GameInfoComponent
import it.unibo.view.component.game.gameboard.sidebar.WindRoseComponent

trait RefreshController extends ActivationController:
  protected def updateAccordingToRefreshType(
      component: GameComponent,
      gameBoard: GameBoard,
      refreshType: RefreshType
  ): Unit =
    given c: GameComponent = component
    given gb: GameBoard = gameBoard

    logger.debug(s"[REFRESH] Type: $refreshType")

    refreshType match
      case RefreshType.PatternChosen => // updateMove(_.lastPatternChosen)
      case CardDraw                  => updateHand
      case CardDiscard               => updateHand
      case CardSelected              =>
        updateMove(_.lastCardChosen)
        updateHand
      case CardDeselected            => updateHand
      case WindUpdate                => updateWind
      case PhaseUpdate               => updatePhase

  private def updateHand(using c: GameComponent, gb: GameBoard): Unit = c
    .updatePlayer(gb.getCurrentPlayer)(gb.gamePhase)

  private def updateWind(using c: GameComponent, gb: GameBoard): Unit = c.sidebarComponent
    .components.foreach:
      case c: WindRoseComponent => c.updateWindRoseDirection(gb.board.windDirection)
      case _                    =>

  private def updatePhase(using c: GameComponent, gb: GameBoard): Unit =
    val currentGamePhase = gb.gamePhase
    gb.getCurrentPlayer match
      case b: Bot    => c.disableView() // TODO why not everything is disabled?
      case p: Person => updateGamePhaseActivation(currentGamePhase)
      case _         =>

    c.updateGrid(gb, currentGamePhase)

    updateHand
    gb.gamePhase match
      case WindPhase =>
        updateMove(_.lastPatternChosen)
        updateWind
      case _         =>

    c.sidebarComponent.components.foreach:
      case c: GameInfoComponent =>
        c.updateTurnPhase(currentGamePhase.toString)
        c.updateTurnNumber(gb.turnNumber)
        c.updateTurnPlayer(gb.getCurrentPlayer.name)
      case _                    =>

  private def updateMove(
      getMove: Player => Option[Move]
  )(using c: GameComponent, gb: GameBoard): Unit = getMove(gb.getCurrentPlayer) match
    case None       => // do not update the grid
    case Some(move) => handleMove(move)

  private def handleMove(move: Move)(using c: GameComponent, gb: GameBoard): Unit =
    move.effect match
      case CardChosen(card, patterns) if move.turnNumber == gb.turnNumber =>
        logger.info(s"[REFRESH] ${gb.getCurrentPlayer.name} ${card.title}")
        c.gridComponent.setAvailablePatterns(patterns, card.effect.effectId)
      case MoveEffect.PatternChosen(patterns)                             =>
        logger.info(s"[REFRESH] ${gb.getCurrentPlayer.name} $patterns")
        c.gridComponent.setAvailablePatterns(patterns, -1)
      case _ => // do not update the grid

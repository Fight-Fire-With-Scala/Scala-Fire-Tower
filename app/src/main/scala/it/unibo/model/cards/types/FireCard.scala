package it.unibo.model.cards.types

import it.unibo.model.cards.resolvers.{EffectResolver, MetaResolver, MultiStepResolver}
import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{LargeEffect, MediumEffect, VerySmallEffect, VeryLargeEffect}
import it.unibo.model.gameboard.grid.{Fire, Firebreak, Token}
import it.unibo.model.prolog.Scala2P.given
import it.unibo.model.prolog.PrologSolver.engine

enum FireCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case Explosion
      extends FireCard(
        id = 0,
        effect = MultiStepResolver(
          VeryLargeEffect(Map("a" -> Fire, "b" -> Firebreak)),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )
  case Flare
      extends FireCard(
        id = 1,
        effect = MultiStepResolver(
          MediumEffect(FireCard.defaultTokens),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )
  case BurningSnag
      extends FireCard(
        id = 2,
        effect = MultiStepResolver(
          LargeEffect(FireCard.defaultTokens),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )
  case Ember
      extends FireCard(
        id = 3,
        effect = MultiStepResolver(
          VerySmallEffect(FireCard.defaultTokens),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )

object FireCard:
  val defaultTokens: Map[String, Token] = Map("a" -> Fire)
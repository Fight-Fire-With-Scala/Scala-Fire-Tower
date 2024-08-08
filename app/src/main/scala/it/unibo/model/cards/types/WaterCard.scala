package it.unibo.model.cards.types

import it.unibo.model.cards.choices.GameChoice
import it.unibo.model.cards.effects.{LargeEffect, MediumEffect, VerySmallEffect, VeryLargeEffect}
import it.unibo.model.cards.resolvers.{EffectResolver, MetaResolver, MultiStepResolver}
import it.unibo.model.gameboard.grid.{Fire, Token, Water}
import it.unibo.model.prolog.PrologSolver.engine
import it.unibo.model.prolog.Scala2P.given

enum WaterCard(
    override val id: Int,
    override val effect: MetaResolver[? <: GameChoice, ? <: EffectResolver]
) extends HasEffect:
  case SmokeJumper
      extends WaterCard(
        id = 11,
        effect = MultiStepResolver(
          VeryLargeEffect(Map("a" -> Water, "b" -> Fire)),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )
  case AirDrop
      extends WaterCard(
        id = 12,
        effect = MultiStepResolver(
          MediumEffect(WaterCard.defaultTokens),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )
  case FireEngine
      extends WaterCard(
        id = 13,
        effect = MultiStepResolver(
          LargeEffect(WaterCard.defaultTokens),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )
  case Bucket
      extends WaterCard(
        id = 14,
        effect = MultiStepResolver(
          VerySmallEffect(WaterCard.defaultTokens),
          engine("""
            | cell(2, 1, X)
            |""".stripMargin)
        )
      )

object WaterCard:
  val defaultTokens: Map[String, Token] = Map("a" -> Water)

package it.unibo.model.prolog.decisionmaking

import alice.tuprolog.Theory
import it.unibo.model.effect.core.ILogicEffect

final case class AllCardsResultTheory(cards: Map[Int, List[ILogicEffect]])

object AllCardsResultTheory:
  def apply(cards: Map[Int, List[ILogicEffect]]): Theory =

    val findallClauses = cards.flatMap { case (cardId, logicEffects) =>
      logicEffects.map { logicEffect =>
        logicEffect.goals.map { goal =>
          val term = goal(cardId).term
          val firstVar = term.toString.split("[(),]").find(_.startsWith("_")).getOrElse("Coords")
          val updatedTerm = term.toString.replaceFirst("Coords", firstVar)
          s"findall(($firstVar, $cardId), $updatedTerm, R$cardId)"
        }.mkString(",\n    ")
      }
    }.mkString(",\n    ")

    val resultVars = cards.keys.map(cardId => s"R$cardId").mkString(", ")
    val concatClause = s"concat_lists([$resultVars], R)"

    val theoryString = s"""
                          |get_all_cards_result(R) :-
                          |    $findallClauses,
                          |    $concatClause.
    """.stripMargin

    Theory.parseWithStandardOperators(theoryString)
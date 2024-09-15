package it.unibo.model.prolog.decisionmaking

import alice.tuprolog.Theory
import it.unibo.model.effect.core.ILogicEffect

final case class AllCardsResultTheory(cards: Map[Int, List[ILogicEffect]])

object AllCardsResultTheory:
  def apply(cards: Map[Int, List[ILogicEffect]]): Theory =

    val findallClauses = cards
      .flatMap: (cardId, logicEffects) =>
        logicEffects
          .flatMap(_.computations)
          .zipWithIndex
          .map: (logicComputation, idx) =>
            val goal = logicComputation.goal
            val term = goal(cardId, idx).term
            val firstVar =
              term.toString.split("[(),]").find(_.startsWith("_")).getOrElse("Coords")
            val updatedTerm = term.toString.replaceFirst("Coords", firstVar)
            s"findall(($firstVar, $cardId), $updatedTerm, R${cardId.abs})"
      .mkString(",\n    ")

    val resultVars   = cards.keys.map(cardId => s"R${cardId.abs}").mkString(", ")
    val concatClause = s"concat_lists([$resultVars], R)"

    val theoryString = s"""
                          |get_all_cards_result(R) :-
                          |    $findallClauses,
                          |    $concatClause.
    """.stripMargin

    Theory.parseWithStandardOperators(theoryString)

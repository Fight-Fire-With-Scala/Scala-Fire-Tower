package it.unibo.model.reasoner.decisionmaking

import alice.tuprolog.Theory
import it.unibo.model.effect.core.LogicEffect
import it.unibo.model.reasoner.ReasonerUtils.defaultId

final case class AllCardsResultTheory(cards: Map[Int, List[LogicEffect]])

object AllCardsResultTheory:
  def apply(cards: Map[Option[Int], List[LogicEffect]]): Theory =
    val findallClauses = cards
      .flatMap: (cardId, logicEffects) =>
        logicEffects
          .flatMap(_.computations)
          .zipWithIndex
          .map: (logicComputation, idx) =>
            val id   = cardId.getOrElse(defaultId.toInt)
            val goal = logicComputation.goal
            val term = goal(Some(id), Some(idx)).term
            val firstVar =
              term.toString.split("[(),]").find(_.startsWith("_")).getOrElse("Coords")
            val updatedTerm = term.toString.replaceFirst("Coords", firstVar)
            s"findall(($firstVar, $id), $updatedTerm, R$id)"
      .mkString(",\n    ")

    val resultVars   = cards.keys.map(cardId => s"R${cardId.getOrElse(defaultId)}").mkString(", ")
    val concatClause = s"concat_lists([$resultVars], R)"
    val theoryString = s"""
                          |get_all_cards_result(R) :-
                          |    $findallClauses,
                          |    $concatClause.
    """.stripMargin

    Theory.parseWithStandardOperators(theoryString)

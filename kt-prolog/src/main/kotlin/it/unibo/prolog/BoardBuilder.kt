package it.unibo.prolog

import it.unibo.tuprolog.core.*
import it.unibo.tuprolog.theory.Theory

class BoardKBBuilder(
    private val board: Map<Position, BoardEntity>,
    private val pattern: Map<Position, Token>,
    private val directions: List<Directions>
) {

    fun getBoardTheory(): Theory {
        val cellFacts: List<Clause> = getCells(board)
        val patternFacts: List<Clause> = getPattern(pattern)
        val directionFacts: Clause = getDeltas(directions)
        val allFacts = listOf(cellFacts, patternFacts).flatten().plus(directionFacts)

        return Theory.of(allFacts)
    }

    private fun getCells(board: Map<Position, BoardEntity>): List<Fact> {
        val cells =
            board.map { (position, cell) ->
                Fact.of(
                    Struct.of(
                        "cell",
                        Tuple.of(Integer.of(position.x), Integer.of(position.y)),
                        Atom.of(cell.id)))
            }

        val positions = board.map { (position, _) -> position }
        val maxX = positions.maxBy { it.x }.x + 1
        val maxY = positions.maxBy { it.y }.y + 1

        val numRows = Fact.of(Struct.of("numRows", Integer.of(maxX)))
        val numCols = Fact.of(Struct.of("numCols", Integer.of(maxY)))

        return cells.plus(numRows).plus(numCols)
    }

    private fun getPattern(pattern: Map<Position, Token>): List<Fact> {
        val patterns =
            pattern.map { (position, token) ->
                Fact.of(
                    Struct.of(
                        "pattern",
                        Tuple.of(Integer.of(position.x), Integer.of(position.y)),
                        Atom.of(token.id)))
            }

        val directionsOfApplication =
            if (pattern.size <= 1) listOf(Directions.North) else Directions.values().toList()

        val directionNames = directionsOfApplication.map { it.id }.map { Atom.of(it) }

        val directionsFact =
            Fact.of(Struct.of("directions", it.unibo.tuprolog.core.List.of(directionNames)))

        return patterns.plus(directionsFact)
    }

    private fun getDeltas(directions: List<Directions>): Fact {
        val directionDeltas =
            directions.map { it.delta }.map { Tuple.of(Integer.of(it.x), Integer.of(it.y)) }

        val deltasFact =
            Fact.of(Struct.of("deltas", it.unibo.tuprolog.core.List.of(directionDeltas)))
        return deltasFact
    }
}

package it.unibo.prolog

import it.unibo.tuprolog.core.Atom
import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Var

fun main() {
    val board =
        mapOf(
            Position(0, 0) to Token.Fire,
            Position(0, 1) to Cell.Woods,
            Position(0, 2) to Cell.Woods,
            Position(1, 0) to Cell.Woods,
            Position(1, 1) to Cell.Woods,
            Position(1, 2) to Cell.Woods,
            Position(2, 0) to Cell.Woods,
            Position(2, 1) to Cell.Woods,
            Position(2, 2) to Cell.Woods,
        )

    val pattern = mapOf(Position(0, 0) to Token.Fire, Position(0, 1) to Token.Fire)

    val directions = Directions.values().toList()
    println(searchValidPatterns(board, pattern, directions))
}

fun searchValidPatterns(
    board: Map<Position, BoardEntity>,
    pattern: Map<Position, Token>,
    directions: List<Directions>
): MutableMap<Position, BoardEntity> {
    val r = Var.of("R")
    val goal = Struct.of("main", r, Atom.of("f"), Var.anonymous(), Var.anonymous())

    val solver = PrologSolver("solver.pl")
    return solver.solve(board, pattern, directions, goal, r)
}

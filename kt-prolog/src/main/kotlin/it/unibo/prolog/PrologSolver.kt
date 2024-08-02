package it.unibo.prolog

import it.unibo.tuprolog.core.Struct
import it.unibo.tuprolog.core.Term
import it.unibo.tuprolog.core.Var
import it.unibo.tuprolog.solve.Solution
import it.unibo.tuprolog.solve.Solver
import it.unibo.tuprolog.theory.Theory
import it.unibo.tuprolog.theory.parsing.ClausesParser
import java.text.ParseException

class PrologSolver(private val solverProgramPath: String) {

    private val prologParser = ClausesParser.withDefaultOperators()

    private fun solverProgram(): String = getResourceAsText(solverProgramPath)!!

    fun solve(
        board: Map<Position, BoardEntity>,
        pattern: Map<Position, Token>,
        directions: List<Directions>,
        goal: Struct,
        results: Var
    ): MutableMap<Position, BoardEntity> {

        val mutableMap: MutableMap<Position, BoardEntity> = mutableMapOf()
        val solution = searchValidPatterns(board, pattern, directions, goal)

        listOf(solution)
            .filterIsInstance<Solution.Yes>()
            .map { it.substitution[results] }
            .forEach { it1 ->
                val l: List<Term> = it1?.castToList()?.toList() ?: emptyList()
                convertToList(l, mutableMap)
            }
        return mutableMap
    }

    private fun searchValidPatterns(
        board: Map<Position, BoardEntity>,
        pattern: Map<Position, Token>,
        directions: List<Directions>,
        goal: Struct
    ): Solution {

        val proceduralTheory = BoardKBBuilder(board, pattern, directions).getBoardTheory()
        val baseTheory =
            try {
                prologParser.parseTheory(solverProgram())
            } catch (e: ParseException) {
                Theory.empty()
            }
        val fullTheory = baseTheory.plus(proceduralTheory)

        val solver = Solver.problog.solverWithDefaultBuiltins(staticKb = fullTheory)
        return solver.solveOnce(goal)
    }

    private fun convertToList(l: List<Term>, mutableMap: MutableMap<Position, BoardEntity>) {
        l.mapNotNull {
            try {
                val ll = it.castToList().toList()
                convertToTuple(ll, mutableMap)
            } catch (ex: ClassCastException) {
                null
            }
        }
    }

    private fun convertToTuple(ll: List<Term>, mutableMap: MutableMap<Position, BoardEntity>) {
        ll.map { li ->
            val args = li.castToStruct().args
            val tuple = args[0].castToTuple()
            val position =
                Position(
                    tuple.left.castToInteger().value.toInt(),
                    tuple.right.castToInteger().value.toInt())
            val token = fromId(args[1].toString(), Token.values()) ?: Token.Empty
            mutableMap.put(position, token)
        }
    }
}

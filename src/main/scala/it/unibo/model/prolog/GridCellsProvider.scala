package it.unibo.model.prolog

import alice.tuprolog.{Struct, Term}
import it.unibo.model.gameboard.grid.Grid
import it.unibo.model.prolog.PrologUtils.given_Conversion_Int_Term
import it.unibo.model.prolog.PrologUtils.given_Conversion_Cell_Term
import it.unibo.model.prolog.PrologUtils.given_Ordering_Struct
import it.unibo.model.prolog.PrologUtils.given_Conversion_Token_Term
import it.unibo.model.prolog.PrologUtils.size

trait GridCellsProvider:
  def getCells(grid: Grid): Iterator[Term] =
    val cells = grid.cells.iterator
      .map { case (pos, cell) =>
        Struct.of("cell", Struct.tuple(pos._1, pos._2), cell)
      }
      .toSeq
      .sorted
      .iterator

    val tokens = grid.tokens.iterator.map { case (pos, token) =>
      Struct.of("token", Struct.tuple(pos._1, pos._2), token)
    }

    val numRows = Iterator.single(Struct.of("numRows", grid.size))
    val numCols = Iterator.single(Struct.of("numCols", grid.size))

    cells ++ tokens ++ numRows ++ numCols

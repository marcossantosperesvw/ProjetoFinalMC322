package org.chess.pieces;

import java.util.Collection;
import java.util.function.Function;

import org.chess.Color;
import org.chess.Move;
import org.chess.PieceNotInBoard;
import org.chess.Pos;

public abstract class NonKing extends Piece {
  public NonKing(Color color) {
    super(color);
  }

  public static record MovesCalcResult(Collection<Move> moves, Collection<Pos> dependencies) {}
  
  public abstract MovesCalcResult calculateMoves(Function<Pos, Piece> gePiece, Function<Piece, Pos> getPos) throws PieceNotInBoard;
}

package org.chess.pieces;

import java.util.Collection;

import org.chess.Color;
import org.chess.Move;
import org.chess.PieceNotInBoard;
import org.chess.Pos;

import com.google.common.collect.BiMap;

public abstract class Piece {
  public final Color color;
  
  public Piece(Color color) {
    this.color = color;
  }

  public static record MovesCalcResult(Collection<Move> moves, Collection<Pos> dependencies) {}
  
  public abstract MovesCalcResult calculateMoves(BiMap<Pos, Piece> boardState) throws PieceNotInBoard;

}

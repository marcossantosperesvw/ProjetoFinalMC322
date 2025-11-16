package org.chess.pieces;

import org.chess.Color;
import org.chess.PieceNotInBoard;
import org.chess.Pos;

import com.google.common.collect.BiMap;

public class Queen extends NonKing {
	public Queen(Color color) {
		super(color);
	}

	@Override
	public MovesCalcResult calculateMoves(BiMap<Pos, Piece> boardState) throws PieceNotInBoard {
		throw new UnsupportedOperationException("Unimplemented method 'calculateMoves'");
	}
}

package org.chess.pieces;

import java.util.function.Function;

import org.chess.Color;
import org.chess.PieceNotInBoard;
import org.chess.Pos;

public class Queen extends NonKing {
	public Queen(Color color) {
		super(color);
	}

	@Override
	public MovesCalcResult calculateMoves(Function<Pos, Piece> gePiece, Function<Piece, Pos> getPos)
			throws PieceNotInBoard {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateMoves'");
	}
}

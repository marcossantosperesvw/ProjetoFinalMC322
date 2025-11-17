package org.chess.pieces;

import java.util.function.Function;

import org.chess.Color;
import org.chess.PieceNotInBoard;
import org.chess.Pos;

public class Rook extends NonKing {
	public Rook(Color color) {
		super(color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MovesCalcResult calculateMoves(Function<Pos, Piece> gePiece, Function<Piece, Pos> getPos)
			throws PieceNotInBoard {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateMoves'");
	}
}

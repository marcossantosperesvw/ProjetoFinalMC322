package org.chess.board;

import org.chess.Color;
import org.chess.Piece;

public class Pawn extends Piece {

	public Pawn(Color color, Board board) {
		super(color, board);
	}

	@Override
	public MovesCalcResult calculateMoves() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calculateMoves'");
	}
}

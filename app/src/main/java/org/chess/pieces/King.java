package org.chess.pieces;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import org.chess.Color;
import org.chess.Move;
import org.chess.PieceNotInBoard;
import org.chess.Pos;

import com.google.common.collect.BiMap;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	public static Collection<Move> calculateMoves(
			Collection<King> kings,
			BiMap<Pos, Piece> getPos,
			Function<Color, Predicate<Pos>> dangerMap) throws PieceNotInBoard {

		throw new UnsupportedOperationException("Unimplemented method 'calculateMoves'");
	}
}

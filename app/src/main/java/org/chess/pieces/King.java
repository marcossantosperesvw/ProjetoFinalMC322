package org.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import org.chess.Color;
import org.chess.board.Board;
import org.chess.Pos;
import org.chess.Move.MoveType;
import org.chess.Move;

public class King extends Piece {
	private Piece kingSideRook;
	private Piece queenSideRook;
	private int row;
	private int column;
	private int[] directionHelper;

	public King(Color color, Board board) {
		super(color, board);
		
		Pos kingPos = super.board.getPos(this);
		row = kingPos.row();
		column = kingPos.column();
		switch(color){
			case GREEN:
				//king side rook position in relation to king
				//queen side rook position in relation to king
				//information to iterate until rooks
				directionHelper = new int[]{0, 3, 0, -4, column, 1};
			case YELLOW:
				directionHelper = new int[]{3, 0, -4, 0, 1, row, 1};
			case RED:
				directionHelper = new int[]{0, -3, 0, 4, -1, column, -1};
			case BLUE:
				directionHelper = new int[]{-3, 0, 4, 0, -1, row, -1};
		}
		kingSideRook = super.board.getPiece(new Pos(row + directionHelper[0], column + directionHelper[1]));
		queenSideRook = super.board.getPiece(new Pos(row + directionHelper[2], column + directionHelper[3]));

	}

	@Override
	public MovesCalcResult calculateMoves() {
		ArrayList<Move> validMoves = new ArrayList<Move>();
		ArrayList<Piece> piecesBlockingMoves = new ArrayList<Piece>();

		//check castling
		List<Move> kingHistory = board.history.getMovesView(this);
		List<Move> kingsideHistory = board.history.getMovesView(kingSideRook);
		List<Move> queensideHistory = board.history.getMovesView(queenSideRook);
		if(kingHistory.isEmpty()){
			//king side castling check
			if(kingsideHistory.isEmpty()){
				for(int i = directionHelper[4]+1; i < directionHelper[4] + (3 * directionHelper[5]); i = i + directionHelper[5]){
					Pos tempPos = null;
					Pos castlingPos = null;
					if(directionHelper[4] == column){
						tempPos = new Pos(row, i);
						castlingPos = new Pos(row, column + (2 * directionHelper[5]));
					}else{
						tempPos = new Pos(i, column);
						castlingPos = new Pos(row + (2 * directionHelper[5]), column);
					}
					Piece pieceInPos = super.board.getPiece(tempPos);
					if(pieceInPos != null){
						piecesBlockingMoves.add(pieceInPos);
					}else if(pieceInPos == kingSideRook){
						checkForDanger(MoveType.KINGSIDE_CASTLING, castlingPos);
					}
				}
			}
			//queen side castling check
			if(queensideHistory.isEmpty()){
				for(int i = directionHelper[4]+1; i < directionHelper[4] - (4 * directionHelper[5]); i = i + directionHelper[5]){
					Pos tempPos = null;
					Pos castlingPos = null;
					if(directionHelper[4] == column){
						tempPos = new Pos(row, i);
						castlingPos = new Pos(row, column + (2 * directionHelper[5]));
					}else{
						tempPos = new Pos(i, column);
						castlingPos = new Pos(row + (2 * directionHelper[5]), column);
					}
					Piece pieceInPos = super.board.getPiece(tempPos);
					if(pieceInPos != null){
						piecesBlockingMoves.add(pieceInPos);
					}else if(pieceInPos == kingSideRook){
						checkForDanger(MoveType.QUEENSIDE_CASTLING, castlingPos);
					}
				}
			}	
		}

		//Normal King Move
		int[][] possibleMoves = {
			{row+1, column},
			{row+1, column+1},
			{row, column+1},
			{row-1, column+1},
			{row-1, column},
			{row-1, column-1},
			{row, column-1},
			{row+1, column-1}
		};
		for(int[] pos : possibleMoves){
			try{
				Pos tempPos = new Pos(pos[0], pos[1]);
				Piece pieceInPos = super.board.getPiece(tempPos);
				if(pieceInPos != null){
					if(pieceInPos.color == super.color){
						piecesBlockingMoves.add(pieceInPos);
					}else{
						checkForDanger(MoveType.SIMPLE_MOVE, tempPos);
					}
				}else{
					checkForDanger(MoveType.SIMPLE_MOVE, tempPos);
				}
				
			}catch(Exception e){}
		}


		return new MovesCalcResult(validMoves, piecesBlockingMoves);
	}
	/**
	 * Verifies if the king would be in danger if moved
	 * to a position, if not, add valid move to that position,
	 * if yes, adds pieces that put king in danger to piecesBlockingMove
	 * @param type
	 * @param pos
	 */
	private void checkForDanger(MoveType type, Pos pos){

	}

}

package org.chess.pieces;

import java.util.ArrayList;

import org.chess.Color;
import org.chess.board.Board;
import org.chess.Move;
import org.chess.Pos;
import org.chess.Move.MoveType;

public class Knight extends Piece{

    public Knight(Color color, Board board){
        super(color, board);
    }

    

    public MovesCalcResult calculateMoves(){
        //This will be the MovesCalcResult atributes
        ArrayList<Move> validMoves = new ArrayList<Move>();
        ArrayList<Piece> piecesBlockingMoves = new ArrayList<Piece>();

        //getting this piece's position
        Pos thisPos = super.board.getPos(this);
        int row = thisPos.row();
        int column = thisPos.column();

        int[][] possibleMoves = {
            {row + 3, column + 1},
            {row + 3, column - 1},
            {row - 3, column + 1},
            {row - 3, column - 1},
            {row + 1, column + 3},
            {row - 1, column + 3},
            {row + 1, column - 3},
            {row - 1, column - 3}
        };

        //creating a list of valid positions in the board

        //Checks if those positions would generate validMoves,
        //then, fills validMoves and piecesBlockingMoves
        for(int[] pos : possibleMoves){
            try{
                Pos tempPos = new Pos(pos[0], pos[1]);
                Piece pieceInPos = super.board.getPiece(tempPos);
                if(pieceInPos != null){
					if(pieceInPos.color == super.color){
						piecesBlockingMoves.add(pieceInPos);
					}else{
						validMoves.add(new Move(this, MoveType.SIMPLE_MOVE, tempPos));
					}
				}else{
					validMoves.add(new Move(this, MoveType.SIMPLE_MOVE, tempPos));
				}
                
            }catch(IllegalArgumentException e){}
        }

        return new MovesCalcResult(validMoves, piecesBlockingMoves);
        
    }

}

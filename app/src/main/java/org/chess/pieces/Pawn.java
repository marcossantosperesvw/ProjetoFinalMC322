package org.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import org.chess.Color;
import org.chess.board.Board;

import com.google.common.collect.BiMap;

import org.chess.Move;
import org.chess.PieceNotInBoard;
import org.chess.Pos;
import org.chess.Move.MoveType;


public class Pawn extends NonKing{
    
    public Pawn(Color color){
        super(color);
    }

    public MovesCalcResult calculateMoves(BiMap<Pos, Piece> boardState) throws PieceNotInBoard{

        //Checks if piece is on the board
        Pos thisPos = boardState.inverse().get(this);
        if(thisPos == null){
            throw new PieceNotInBoard();
        }
        
        //This will be the MovesCalcResult atributes
        ArrayList<Move> validMoves = new ArrayList<Move>();
        ArrayList<Pos> dependencies = new ArrayList<Pos>();

        //getting this piece's positionu
        int row = thisPos.row();
        int column = thisPos.column();

        }
        //Double-step check
        List<Move> thisMoves = super.board.history.getMovesView(this);
        if(thisMoves.isEmpty()){
            Pos tempPos = new Pos(row + 2 * directionHelper[8], column + 2 * directionHelper[9]);
            Piece pieceInPos = super.board.getPiece(tempPos);
            if(pieceInPos == null){
                validMoves.add(new Move(this, MoveType.PAWN_DOUBLE, tempPos));
            }else{
                dependencies.add(pieceInPos);
            }
        }
        

        //Simple Move check
        int tempRow = row + directionHelper[8];
        int tempColumn = column + directionHelper[9];
        Pos tempPos = new Pos(tempRow, tempColumn);
        Piece pieceInPos = super.board.getPiece(tempPos);
        if(pieceInPos == null){
            if(tempRow == 1 || tempRow == 14 || tempColumn == 1 || tempColumn == 14){
                validMoves.add(new Move(this, MoveType.QUEEN_PROMOTION, tempPos));
                validMoves.add(new Move(this, MoveType.KNIGHT_PROMOTION, tempPos));
                validMoves.add(new Move(this, MoveType.ROOK_PROMOTION, tempPos));
                validMoves.add(new Move(this, MoveType.BISHOP_PROMOTION, tempPos));
            }else{
                validMoves.add(new Move(this, MoveType.SIMPLE_MOVE, tempPos));
            }  
        }else{
            dependencies.add(pieceInPos);
        }

        //Left Attack-Move check
        try{
            tempRow = row + directionHelper[4];
            tempColumn = column + directionHelper[5];
            tempPos = new Pos(tempRow, tempColumn);
            pieceInPos = super.board.getPiece(tempPos);
            if(pieceInPos != null && pieceInPos.color != super.color){
                if(tempRow == 1 || tempRow == 14 || tempColumn == 1 || tempColumn == 14){
                    validMoves.add(new Move(this, MoveType.QUEEN_PROMOTION, tempPos));
                    validMoves.add(new Move(this, MoveType.KNIGHT_PROMOTION, tempPos));
                    validMoves.add(new Move(this, MoveType.ROOK_PROMOTION, tempPos));
                    validMoves.add(new Move(this, MoveType.BISHOP_PROMOTION, tempPos));
                }else{
                    validMoves.add(new Move(this, MoveType.SIMPLE_MOVE, tempPos));
                }  
            }
            
        }catch(Exception e){}

        //Right Attack-Move check
        try{
            tempRow = row + directionHelper[6];
            tempColumn = column + directionHelper[7];
            tempPos = new Pos(tempRow, tempColumn);
            pieceInPos = super.board.getPiece(tempPos);
            if(pieceInPos != null && pieceInPos.color != super.color){
                if(tempRow == 1 || tempRow == 14 || tempColumn == 1 || tempColumn == 14){
                    validMoves.add(new Move(this, MoveType.QUEEN_PROMOTION, tempPos));
                    validMoves.add(new Move(this, MoveType.KNIGHT_PROMOTION, tempPos));
                    validMoves.add(new Move(this, MoveType.ROOK_PROMOTION, tempPos));
                    validMoves.add(new Move(this, MoveType.BISHOP_PROMOTION, tempPos));
                }else{
                    validMoves.add(new Move(this, MoveType.SIMPLE_MOVE, tempPos));
                }  
            }
        }catch(Exception e){}

        return new MovesCalcResult(validMoves, dependencies);
    }
}

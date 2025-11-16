package org.chess.board;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.chess.Color;
import org.chess.Move;
import org.chess.PieceNotInBoard;
import org.chess.pieces.King;
import org.chess.pieces.NonKing;
import org.chess.pieces.Piece;
import org.chess.Pos;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

/**
 * Manages the relation between each piece and its position.
 * Also calculates which pieces are in danger.
 */
class BoardState {
  // ##################################################
  // Data structures
  // ##################################################

  /**
   * A bi-directional map between Piece and its Pos.
   */
  private final BiMap<Pos, Piece> boardState = HashBiMap.create();

  /**
   * It is used to find which pieces' moves need to be recalculated after a change
   * in the board's state. It should only be mutated within the `reevaluate`
   * method. Read the method's docstring to know when it must be called.
   */
  private final Dependencies dependencies = new Dependencies();

  /**
   * It is used to store pieces' moves. It should only be mutated within the
   * `reevaluate` method. Read the method's docstring to know when it must be
   * called.
   */
  private final PossibleMoves moves = new PossibleMoves();

  private final Map<Color, King> kingsMap = new EnumMap<>(Color.class);

  // ##################################################
  // Constructor
  // ##################################################

  BoardState() {
    // TODO: when starting a new game, all pieces are added. But the `add` method
    // reevaluates for every piece. which is not necessary. Maybe the constructor
    // should receice the initial state??
  }

  // ##################################################
  // Read only operations
  // ##################################################

  /**
   * @param piece
   * @return The position of piece.
   * @throws IllegalArgumentException if piece is not on the board.
   */
  Pos getPos(Piece piece) {
    Pos pos = boardState.inverse().get(piece);
    if (pos == null) {
      throw new IllegalArgumentException("Invalid piece: This piece is not on the board.");
    }
    return pos;
  }

  /**
   * @param pos
   * @return piece at pos or null if the position is empty.
   */
  Piece getPiece(Pos pos) {
    return boardState.get(pos);
  }

  /**
   * @param pos
   * @param color
   * @return true if color would be in danget at pos. Else, false.
   */
  boolean isDangerous(Pos pos, Color color) {
    return moves.isDangerous(pos, color);
  }

  Collection<Move> getReadonlyMoves(Piece piece) {
    return moves.getReadonly(piece);
  }

  // ##################################################
  // Mutating operations
  // ##################################################

  /**
   * @param piece
   * @param pos
   * @throws IllegalArgumentException if parameters are null, or if there's
   *                                  already a
   *                                  piece at pos, or if the piece is already on
   *                                  the board.
   */
  void addPiece(Piece piece, Pos pos) {
    Objects.requireNonNull(piece, "piece should not be null.");
    Objects.requireNonNull(pos, "pos should not be null.");

    if (boardState.get(pos) != null) {
      throw new IllegalArgumentException("Invalid Position: There's already a piece at this position.");
    }

    try {
      boardState.put(pos, piece);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid Piece: This piece is already at another position. Use .move instead.");
    }

    if (piece instanceof King king) {
      if (kingsMap.get(king.color) != null)
        throw new IllegalArgumentException("Invalid King: cannot add two kings with the same color.");
      kingsMap.put(king.color, king);
    }

    Collection<Piece> needReevaluation = dependencies.getDependents(pos);
    needReevaluation.add(piece);
    reevaluate(needReevaluation);
  }

  /**
   * @param piece
   * @throws IllegalArgumentException if piece is not on the board
   */
  void removePiece(Piece piece) {
    reevaluate(removePieceHelper(piece));
  }

  /**
   * @param piece
   * @param toPos
   * @return The piece that was taken, if any.
   * @throws IllegalArgumentException if piece is not on the board
   */
  Piece movePiece(Piece piece, Pos toPos) {
    Pos fromPos = boardState.inverse().get(piece);
    if (fromPos == null) {
      throw new IllegalArgumentException("Invalid piece: This piece is not on the board.");
    }

    Collection<Piece> needReevaluation = dependencies.getDependents(fromPos);
    needReevaluation.addAll(dependencies.getDependents(toPos));
    needReevaluation.add(piece);

    Piece capturedPiece = getPiece(toPos);
    if (capturedPiece != null) {
      needReevaluation.addAll(removePieceHelper(piece));
    }

    boardState.forcePut(toPos, piece);

    reevaluate(needReevaluation);

    return capturedPiece;
  }

  // ##################################################
  // Private helper functions
  // ##################################################

  private Collection<Piece> removePieceHelper(Piece piece) {
    Pos pos = boardState.inverse().remove(piece);
    if (pos == null) {
      throw new IllegalArgumentException("Invalid Piece: This piece is not on the board.");
    }
    if (piece instanceof King king) {
      kingsMap.remove(king.color);
    }
    Collection<Piece> needReevaluation = dependencies.getDependents(pos);
    needReevaluation.add(piece);
    return needReevaluation;
  }

  /**
   * This is a function to mutate the `dependencies` and `moves` data structures.
   * Every time a change happens to a position (i.e.: a piece moves to it, or a
   * piece is added to it, or a piece is removed from it) all pieces that depend
   * on that position must be reevaluated, as well as the piece that got
   * added/removed/moved.
   * 
   * @param pieces that need to be reevaluated
   */
  private void reevaluate(Collection<Piece> pieces) {
    for (Piece piece : pieces) {
      if (piece instanceof NonKing nonKing) {
        reevaluate(nonKing);
      }
    }

    // Since kings can't checkmate themselves, they need to know every move from
    // every piece. Therefore their calculation must be deferred.
    reevaluateKings();
  }

  /**
   * Reevaluation consists of:
   * - Removing from the data structures all stored moves and dependencies from a
   * piece.
   * - Calculating all dependencies and possible moves.
   * - Adding them to the data structures.
   * 
   * Since `calculateMoves` assumes the piece is from the player in the bottom,
   * the board must be rotated before sending it to the piece, and the result must
   * be rotated back.
   */
  private void reevaluate(NonKing piece) {
    moves.removeAll(piece);
    dependencies.removeAll(piece);
    try {
      var calcResult = piece.calculateMoves(
          makeGetPiece(piece.color),
          makeGetPos(piece.color));

      for (Pos pos : calcResult.dependencies()) {
        dependencies.add(piece, pos.fromPerspective(piece.color));
      }

      for (Move m : calcResult.moves()) {
        moves.add(new Move(
            m.piece(),
            m.type(),
            m.movingTo().fromPerspective(piece.color)));
      }
    } catch (PieceNotInBoard e) {
      throw new IllegalStateException("Tried to reevaluate piece that's not on the board");
    }
  }

  private void reevaluateKings() {
    var kings = kingsMap.values();
    for (King king : kings) {
      moves.removeAll(king);
    }
    try {
      King.calculateMoves(kings, Maps.unmodifiableBiMap(boardState), makeDangerMap()).forEach(moves::add);
    } catch (PieceNotInBoard e) {
      throw new IllegalStateException("Tried to reevaluate piece that's not on the board");
    }
  }

  private Function<Piece, Pos> makeGetPos(Color color) {
    return (piece) -> boardState.inverse().get(piece).toPerspective(color);
  }

  private Function<Pos, Piece> makeGetPiece(Color color) {
    return (pos) -> boardState.get(pos.fromPerspective(color));
  }

  private Function<Color, Predicate<Pos>> makeDangerMap() {
    return color -> pos -> moves.isDangerous(pos, color);
  }
}

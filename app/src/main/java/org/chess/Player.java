package org.chess;

import java.util.List;

public record Player(List<Piece> pieces, King king, Clock clock, Color color){
}

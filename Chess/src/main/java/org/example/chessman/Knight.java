package org.example.chessman;

import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece {
    public Knight(Color color) {
        super(color);
    }
    protected final int[][] knightMoveSetDirections = { {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1} };

    @Override
    public String getPieceSymbol() {
        return getColor() == Color.WHITE ? "♘" : "♞";
    }

    @Override
    public String getName() {
        return "knight";
    }
    @Override
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        return new ArrayList<>(canMoveInPreciseDirection(coords, gameBoard, knightMoveSetDirections));
    }

}

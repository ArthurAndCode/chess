package org.example.chessman;

import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessPiece {
    public Queen(Color color) {
        super(color);
    }
    @Override
    public String getPieceSymbol() {
        return getColor() == Color.WHITE ? "♕" : "♛";
    }
    @Override
    public String getName() {
        return "queen";
    }
    @Override
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        List<String> availableCoords = new ArrayList<>();

        availableCoords.addAll(canMoveVertically(coords, gameBoard));
        availableCoords.addAll(canMoveHorizontally(coords, gameBoard));
        availableCoords.addAll(canMoveDiagonally(coords, gameBoard));

        return availableCoords;
    }
}

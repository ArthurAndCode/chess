package org.example.chessman;

import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece {
    protected boolean hasMoved;
    public Rook(Color color, boolean hasMoved) {
        super(color);
        this.hasMoved = hasMoved;
    }

    @Override
    public String getPieceSymbol() {
        return getColor() == Color.WHITE ? "♖" : "♜";
    }
    @Override
    public String getName() {
        return "rook";
    }
    @Override
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        List<String> availableCoords = new ArrayList<>();
        availableCoords.addAll(canMoveVertically(coords, gameBoard));
        availableCoords.addAll(canMoveHorizontally(coords, gameBoard));
        return availableCoords;
    }
}

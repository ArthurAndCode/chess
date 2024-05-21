package org.example.chessman;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessPiece {
    public Bishop(Color color) {
        super(color);
    }
    @Override
    public String getPieceSymbol() {
        return getColor() == Color.WHITE ? "♗" : "♝";
    }
    @Override
    public String getName() {
        return "bishop";
    }
    @Override
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        return new ArrayList<>(canMoveDiagonally(coords, gameBoard));
    }
}

package org.example.chessman;


import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    private final boolean hasMoved;
    private final int[][] kingMoveSetDirections = { {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1} };

    public King(Color color, boolean hasMoved) {
        super(color);
        this.hasMoved = hasMoved;
    }

    protected ArrayList<String> canPerformCastling(String coords, ChessPiece[][] gameBoard) {
        ArrayList<String> availableMoves = new ArrayList<>();
        int[] xy = coordinates.getArrayCoords(coords);
        // Check the side
        King king = (King) gameBoard[xy[0]][xy[1]];
        int side  = (king.getColor() == Color.WHITE) ? 7 : 0;


        if (king.hasMoved) {
            return availableMoves;
        }
        // Check squares for long castling and if the left rook has moved
        if (gameBoard[side][0] != null && gameBoard[side][0] instanceof Rook leftRook) {
            if (gameBoard[side][1] == null && gameBoard[side][2] == null && gameBoard[side][3] == null && !leftRook.hasMoved) {
                availableMoves.add("castling " + coordinates.getChessNotationCoords(new int[]{side, 2}));
            }
        }
        // Check squares for short castling and if the right rook has moved
        if (gameBoard[side][7] != null && gameBoard[side][7] instanceof Rook rightRook) {
            if (gameBoard[side][5] == null && gameBoard[side][6] == null && !rightRook.hasMoved) {
                availableMoves.add("castling " + coordinates.getChessNotationCoords(new int[]{side, 6}));
            }
        }
        return availableMoves;
    }


    @Override
    public String getPieceSymbol() {
        return getColor() == Color.WHITE ? "♔" : "♚";
    }

    @Override
    public String getName() {
        return "king";
    }

    @Override
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        List<String> availableCoords = new ArrayList<>();
        availableCoords.addAll(canMoveInPreciseDirection(coords, gameBoard, kingMoveSetDirections));
        availableCoords.addAll(canPerformCastling(coords, gameBoard));
        return availableCoords;
    }
}

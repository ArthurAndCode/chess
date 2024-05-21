package org.example.chessman;

import static org.example.ChessBoard.lastTurn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pawn extends ChessPiece {
    public Pawn(Color color) {
        super(color);
    }
    private String canMoveForward(String coords, ChessPiece[][] gameBoard)  {
        int[] xy = coordinates.getArrayCoords(coords);
        ChessPiece piece = gameBoard[xy[0]][xy[1]];
        int direction = (piece.getColor() == Color.WHITE) ? -1 : 1;
        return canMoveOneSquareVertically(coords, gameBoard, direction);
    }

    private String canMoveTwoForward(String coords, ChessPiece[][] gameBoard) {
        int[] xy = coordinates.getArrayCoords(coords);
        ChessPiece piece = gameBoard[xy[0]][xy[1]];

        int direction = (piece.getColor() == Color.WHITE) ? -1 : 1;
        int startingRank = (piece.getColor() == Color.WHITE) ? 6 : 1;

        if (xy[0] == startingRank) {
            if (gameBoard[xy[0] + direction][xy[1]] == null && gameBoard[xy[0] + (direction * 2)][xy[1]] == null) {
                return coordinates.getChessNotationCoords(new int[]{xy[0] + (direction * 2), xy[1]});
            }
        }
        return null;
    }
    public ArrayList<String> canCaptureDiagonallyForward(String coords, ChessPiece[][] gameBoard) {
        ArrayList<String> availableMoves = new ArrayList<>();
        int[] xy = coordinates.getArrayCoords(coords);
        ChessPiece piece = gameBoard[xy[0]][xy[1]];
        int direction = (piece.getColor() == Color.WHITE) ? -1 : 1;
        // Check diagonally left and right square
        for (int i = -1; i <= 1; i += 2) {
            int newX = xy[0] + direction;
            int newY = xy[1] + i;

            if (isInsideBorder(newX) && isInsideBorder(newY)) {
                ChessPiece diagonalPiece = gameBoard[newX][newY];
                if (diagonalPiece != null && diagonalPiece.getColor() != piece.getColor()) {
                    availableMoves.add(coordinates.getChessNotationCoords(new int[]{newX, newY}));
                }
            }
        }
        return availableMoves;
    }

    private ArrayList<String> canCaptureEnPassant(String coords, ChessPiece[][] gameBoard) {
        ArrayList<String> availableMoves = new ArrayList<>();

        int[] xy = coordinates.getArrayCoords(coords);
        ChessPiece piece = gameBoard[xy[0]][xy[1]];
        int direction = (piece.getColor() == Color.WHITE) ? -1 : 1;

        for (int i = -1; i <= 1; i += 2) {
            int newX = xy[0] + direction;
            int newY = xy[1] + i;
            if (isInsideBorder(newX) && isInsideBorder(newY) && isEnemyPassant(new int[]{xy[0], newY})) {
                // Available destination
                ChessPiece diagonalPiece = gameBoard[newX][newY];
                // Available capture
                ChessPiece horizontalPiece = gameBoard[xy[0]][newY];
                if (diagonalPiece == null && horizontalPiece instanceof Pawn && horizontalPiece.getColor() != piece.getColor()) {
                    // Return destination
                    availableMoves.add("en passant "+coordinates.getChessNotationCoords(new int[]{newX, newY}));
                }
            }
        }
        return availableMoves;
    }
    private boolean isEnemyPassant(int[] enemyPawnCoords) {
        // lastTurn[0] is Object(instance of ChessPiece),
        // [1] is String(chess piece coordinates before move)
        // [2] is String(chess piece coordinates after move)
        if (lastTurn[0] instanceof Pawn) {
            int[] enemyCurrentCoords = coordinates.getArrayCoords((String) lastTurn[1]);
            int[] enemyDestinationCoords = coordinates.getArrayCoords((String) lastTurn[2]);
            return (Math.abs(enemyCurrentCoords[0] - enemyDestinationCoords[0]) == 2) && Arrays.equals(enemyDestinationCoords, enemyPawnCoords);
        }
        return false;
    }

    private ArrayList<String> canBePromoted(String coords, ChessPiece[][] gameBoard) {
        int[] xy = coordinates.getArrayCoords(coords);
        ChessPiece piece = gameBoard[xy[0]][xy[1]];

        int lastRank = (piece.getColor() == Color.WHITE) ? 0 : 7;

        if (xy[0] == lastRank) {
            return new ArrayList<>(Arrays.asList("rook", "knight", "bishop", "queen"));
        }
        return new ArrayList<>();
    }


    @Override
    public String getPieceSymbol() {
        return getColor() == Color.WHITE ? "♙" : "♟";
    }
    @Override
    public String getName() {
        return "pawn";
    }
    @Override
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        List<String> availableCoords = new ArrayList<>();

        String forwardMove = canMoveForward(coords, gameBoard);
        if (forwardMove != null) {
            availableCoords.add(forwardMove);
        }
        String twoForwardMove = canMoveTwoForward(coords, gameBoard);
        if (twoForwardMove != null) {
            availableCoords.add(twoForwardMove);
        }

        availableCoords.addAll(canCaptureDiagonallyForward(coords, gameBoard));
        availableCoords.addAll(canCaptureEnPassant(coords, gameBoard));
        availableCoords.addAll(canBePromoted(coords, gameBoard));

        return availableCoords;
    }
}

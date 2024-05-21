package org.example.chessman;


import org.example.Coordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class ChessPiece {
    protected final Coordinates coordinates = new Coordinates();
    protected Color color;
    public enum Color {
        BLACK,
        WHITE;
    }
    public ChessPiece(Color color) {
        this.color = color;

    }
    public abstract String getPieceSymbol();
    public abstract String getName();
    public List<String> getAvailableCoordinates(String coords, ChessPiece[][] gameBoard) {
        return null;
    }
    public Color getColor() {
        return color;
    }

    protected boolean isInsideBorder(int coordinate) {
        return coordinate >= 0 && coordinate < 8;
    }

    protected ArrayList<String> canMoveInPreciseDirection(String coords, ChessPiece[][] gameBoard, int[][] directions) {
        ArrayList<String> availableMoves = new ArrayList<>();
        int[] xy = coordinates.getArrayCoords(coords);

        for (int[] direction : directions) {
            int newX = xy[0] + direction[0];
            int newY = xy[1] + direction[1];

            if (isInsideBorder(newX) && isInsideBorder(newY) && (gameBoard[newX][newY] == null ||
                    gameBoard[newX][newY].getColor() != this.color)) {
                availableMoves.add(coordinates.getChessNotationCoords(new int[]{newX, newY}));
            }
        }
        return availableMoves;
    } // int[][] directions are off set X and Y

    protected String canMoveOneSquareVertically(String coords, ChessPiece[][] gameBoard, int direction) {
        int[] xy = coordinates.getArrayCoords(coords);

        if (isInsideBorder(xy[0] + direction) && gameBoard[xy[0] + direction][xy[1]] == null) {
            return coordinates.getChessNotationCoords(new int[]{xy[0] + direction, xy[1]});
        }
        return null;
    }
    protected String canMoveOneSquareHorizontally(String coords, ChessPiece[][] gameBoard, int direction) {
        int[] xy = coordinates.getArrayCoords(coords);

        if (isInsideBorder(xy[1] + direction) && gameBoard[xy[0]][xy[1] + direction] == null) {
            return coordinates.getChessNotationCoords(new int[]{xy[0], xy[1] + direction});
        }
        return null;
    }
    protected ArrayList<String> canMoveVertically(String coords, ChessPiece[][] gameBoard) {
        ArrayList<String> availableCoords = new ArrayList<>();

        int direction = 1;
        String nextMove = canMoveOneSquareVertically(coords, gameBoard, direction);
        while (nextMove != null) {
            availableCoords.add(nextMove);
            direction++;
            nextMove = canMoveOneSquareVertically(coords, gameBoard, direction);
        }
        String captureMove = canCaptureByMoving(coords, direction, 0, gameBoard);
        if (captureMove != null) {
            availableCoords.add(captureMove);
        }

        direction = -1;
        nextMove = canMoveOneSquareVertically(coords, gameBoard, direction);
        while (nextMove != null) {
            availableCoords.add(nextMove);
            direction--;
            nextMove = canMoveOneSquareVertically(coords, gameBoard, direction);
        }
        captureMove = canCaptureByMoving(coords, direction, 0, gameBoard);
        if (captureMove != null) {
            availableCoords.add(captureMove);
        }

        return availableCoords;
    }
    protected ArrayList<String> canMoveHorizontally(String coords, ChessPiece[][] gameBoard) {
        ArrayList<String> availableCoords = new ArrayList<>();

        int direction = 1;
        String nextMove = canMoveOneSquareHorizontally(coords, gameBoard, direction);
        while (nextMove != null) {
            availableCoords.add(nextMove);
            direction ++;
            nextMove = canMoveOneSquareHorizontally(coords, gameBoard, direction);
        }
        String captureMove = canCaptureByMoving(coords, 0, direction, gameBoard);
        if (captureMove != null) {
            availableCoords.add(captureMove);
        }

        direction = -1;
        nextMove = canMoveOneSquareHorizontally(coords, gameBoard, direction);
        while (nextMove != null) {
            availableCoords.add(nextMove);
            direction --;
            nextMove = canMoveOneSquareHorizontally(coords, gameBoard, direction);
        }
        captureMove = canCaptureByMoving(coords, 0, direction, gameBoard);
        if (captureMove != null) {
            availableCoords.add(captureMove);
        }
        return availableCoords;
    }

    protected ArrayList<String> canMoveDiagonally(String coords, ChessPiece[][] gameBoard) {
        ArrayList<String> availableMoves = new ArrayList<>();
        int[] xy = coordinates.getArrayCoords(coords);
        int[][] directions = { {-1, -1}, {-1, 1}, {1, -1}, {1, 1} };

        for (int[] direction : directions) {
            int newX = xy[0] + direction[0];
            int newY = xy[1] + direction[1];

            while (isInsideBorder(newX) && isInsideBorder(newY) && gameBoard[newX][newY] == null) {
                availableMoves.add(coordinates.getChessNotationCoords(new int[]{newX, newY}));
                newX += direction[0];
                newY += direction[1];
            }

            String captureMove = canCaptureByMoving(coords, newX -xy[0], newY-xy[1], gameBoard);
            if (captureMove != null) {
                availableMoves.add(captureMove);
            }
        }
        return availableMoves;
    }

    // Checks the possibility of capturing a piece at a given position by moving specified offsets
    String canCaptureByMoving(String coords, int offSetX, int offSetY, ChessPiece[][] gameBoard) {
        int[] xy = coordinates.getArrayCoords(coords);
        ChessPiece piece = gameBoard[xy[0]][xy[1]];
        int newX = xy[0] + offSetX;
        int newY = xy[1] + offSetY;
        if (isInsideBorder(newX) && isInsideBorder(newY)) {
            ChessPiece capturedPiece = gameBoard[newX][newY];
            if (capturedPiece != null && capturedPiece.getColor() != piece.getColor()) {
                return coordinates.getChessNotationCoords(new int[]{newX, newY});
            }
        }
        return null;
    }
}

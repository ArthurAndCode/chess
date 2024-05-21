package org.example;

import org.example.chessman.*;

import java.util.Arrays;
import java.util.List;



public class ChessBoard {
    private final ChessPiece[][] gameBoard = new ChessPiece[8][8];
    private final Coordinates coordinates = new Coordinates();
    public static final Object[] lastTurn = new Object[3];
    public ChessPiece[][] getGameBoard() {
        return gameBoard;
    }

    private void updateLastTurn(String currentCoords, String destination) {
        lastTurn[0] = getPieceAt(currentCoords);
        lastTurn[1] = currentCoords;
        lastTurn[2] = destination;
    }

    public ChessBoard() {
        initializeGameBoard();
    }

    private void initializeGameBoard() {
        for (int i = 0; i < 8; i++) {
            gameBoard[1][i] = new Pawn(ChessPiece.Color.BLACK);
        }
        gameBoard[0][0] = new Rook(ChessPiece.Color.BLACK, false);
        gameBoard[0][7] = new Rook(ChessPiece.Color.BLACK, false);
        gameBoard[0][1] = new Knight(ChessPiece.Color.BLACK);
        gameBoard[0][6] = new Knight(ChessPiece.Color.BLACK);
        gameBoard[0][2] = new Bishop(ChessPiece.Color.BLACK);
        gameBoard[0][5] = new Bishop(ChessPiece.Color.BLACK);
        gameBoard[0][3] = new Queen(ChessPiece.Color.BLACK);
        gameBoard[0][4] = new King(ChessPiece.Color.BLACK, false);
        for (int i = 0; i < 8; i++) {
            gameBoard[6][i] = new Pawn(ChessPiece.Color.WHITE);
        }
        gameBoard[7][0] = new Rook(ChessPiece.Color.WHITE, false);
        gameBoard[7][7] = new Rook(ChessPiece.Color.WHITE, false);
        gameBoard[7][1] = new Knight(ChessPiece.Color.WHITE);
        gameBoard[7][6] = new Knight(ChessPiece.Color.WHITE);
        gameBoard[7][2] = new Bishop(ChessPiece.Color.WHITE);
        gameBoard[7][5] = new Bishop(ChessPiece.Color.WHITE);
        gameBoard[7][3] = new Queen(ChessPiece.Color.WHITE);
        gameBoard[7][4] = new King(ChessPiece.Color.WHITE, false);
    }

    public void printChessBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = gameBoard[row][col];
                String symbol = (piece == null) ? "⛞" : piece.getPieceSymbol();
                System.out.printf("%-3s", symbol);
            }
            System.out.println();
        }
    }

    public ChessPiece getPieceAt(String coords) {
        int[] xy = coordinates.getArrayCoords(coords);
        return gameBoard[xy[0]][xy[1]];
    }

    public boolean isAccessible(String coords, boolean whiteTurn) {
        if (getPieceAt(coords) == null) {
            return false;
        } // Check if chess piece belong to current player
        if (whiteTurn && getPieceAt(coords).getColor() == ChessPiece.Color.WHITE) {
            return true;
        }
        return !whiteTurn && getPieceAt(coords).getColor() == ChessPiece.Color.BLACK;
    }

    public List<String> getAvailableCoordsForChessPiece(String currentCoords) {
        ChessPiece piece = getPieceAt(currentCoords);
        return piece.getAvailableCoordinates(currentCoords, gameBoard);
    }

    public void makeMove(String currentCoords, String destination) {
        updateLastTurn(currentCoords, destination);

        if (destination.matches("[a-h][1-8]")) {
            move(currentCoords, destination);

        } else if (destination.matches("castling [a-h][1-8]")) {
            castling(currentCoords, destination);

        } else if (destination.matches("en passant [a-h][1-8]")) {
            captureEnPassant(currentCoords, destination);

        } else {
            List<String> promoteOptions = Arrays.asList("rook", "knight", "bishop", "queen");
            for (String option : promoteOptions) {
                if (destination.contains(option)) {
                    promote(currentCoords, destination);
                    break;
                }
            }
        }
    }

    private void captureEnPassant(String coords, String destination) {
        move(coords, destination);
        int[] destinationXY = coordinates.getArrayCoords(destination);
        int[] currentXY = coordinates.getArrayCoords(coords);
        gameBoard[currentXY[0]][destinationXY[1]] = null;
    }

    private void move(String coords, String destination) {
        // set square occupied by chess piece to null
        int[] pieceCoords = coordinates.getArrayCoords(coords);
        int[] destinationCoords = coordinates.getArrayCoords(destination);
        ChessPiece piece = getPieceAt(coords);

        gameBoard[pieceCoords[0]][pieceCoords[1]] = null;
        // create piece at destination square
        if (piece instanceof King) {
            gameBoard[destinationCoords[0]][destinationCoords[1]] = new King(piece.getColor(), true);
        } else if (piece instanceof Rook) {
            gameBoard[destinationCoords[0]][destinationCoords[1]] = new Rook(piece.getColor(), true);
        } else {
            gameBoard[destinationCoords[0]][destinationCoords[1]] = piece;
        }
    }

    private void promote(String coords, String newPiece) {
        ChessPiece piece = getPieceAt(coords);
        ChessPiece.Color color = piece.getColor();
        int[] pieceCoords = coordinates.getArrayCoords(coords);

        gameBoard[pieceCoords[0]][pieceCoords[1]] = switch (newPiece) {
            case "rook" -> new Rook(color, true);
            case "knight" -> new Knight(color);
            case "bishop" -> new Bishop(color);
            default -> new Queen(color);

        };
    }

    private void castling(String currentCoords, String destination) {
        String destinationCoords = destination.substring(9);
        ChessPiece piece = getPieceAt(currentCoords);
        int side  = (piece.getColor() == ChessPiece.Color.WHITE) ? 7 : 0;
        // king move
        move(currentCoords, destinationCoords);
        // rook move
        int[] xy = coordinates.getArrayCoords(destination);
        if (xy[1] == 2) {
            move(coordinates.getChessNotationCoords(new int[] {side,0}), (coordinates.getChessNotationCoords(new int[] {side,3})));
        } else {
            move(coordinates.getChessNotationCoords(new int[] {side,7}), (coordinates.getChessNotationCoords(new int[] {side,5})));
        }
    }
}

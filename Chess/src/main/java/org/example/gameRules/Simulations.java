package org.example.gameRules;

import org.example.Coordinates;
import org.example.chessman.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulations {
    protected final Coordinates coordinates = new Coordinates();

    public ChessPiece[][] createHypotheticalBoard(ChessPiece[][] gameBoard, String currentCoords, String destinationCoords) {
        ChessPiece[][] hypotheticalBoard = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            hypotheticalBoard[i] = gameBoard[i].clone();
        }
        // Make hypothetical move on hypothetical board; return board
        int[] fromXY = coordinates.getArrayCoords(currentCoords);
        int[] toXY = coordinates.getArrayCoords(destinationCoords);
        hypotheticalBoard[toXY[0]][toXY[1]] = hypotheticalBoard[fromXY[0]][fromXY[1]];
        hypotheticalBoard[fromXY[0]][fromXY[1]] = null;
        return hypotheticalBoard;
    }

    private String findKing(ChessPiece.Color kingColor, ChessPiece[][] gameBoard) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = gameBoard[row][col];
                if (piece instanceof King && piece.getColor() == kingColor) {
                    return coordinates.getChessNotationCoords(new int[]{row, col});
                }
            }
        }
        return null;
    }

    protected boolean isKingInCheck(ChessPiece.Color kingColor, ChessPiece[][] board) {
        String kingCoordinates = findKing(kingColor, board);
        ArrayList<String> attackedCoords = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                // Get coordinates of squares under attack
                if (piece != null) {
                    String pieceCoords = coordinates.getChessNotationCoords(new int[]{row, col});
                    if (piece.getColor() != kingColor && !(piece instanceof Pawn)) {
                        attackedCoords.addAll(piece.getAvailableCoordinates(pieceCoords, board));
                    } else if (piece.getColor() != kingColor && piece instanceof Pawn) {
                        ((Pawn) piece).canCaptureDiagonallyForward(pieceCoords, board);
                    }
                }
            }
        }
        return attackedCoords.contains(kingCoordinates);
    }

    public List<String> filterSafeMoves(List<String> availableMoves, String currentCoords, ChessPiece.Color kingColor, ChessPiece[][] board) {
        List<String> safeMoves = new ArrayList<>();

        for (String move : availableMoves) {
            if (move.matches("[a-h][1-8]") && filterMove(move, currentCoords, kingColor, board) != null) {
                safeMoves.add(move);

            } else if (move.matches("castling [a-h][1-8]") &&
                    filterCastling(move.substring(9), currentCoords, kingColor, board) != null) {
                safeMoves.add(move);
            } else if (move.matches("en passant [a-h][1-8]") &&
                    filterEnPassant(move.substring(11), currentCoords, kingColor, board) != null) {
                safeMoves.add(move);
            }
            List<String> promoteOption = Arrays.asList("rook", "knight", "bishop", "queen");
            for (String option : promoteOption) {
                if (availableMoves.contains(option)) {
                    safeMoves.add(move);
                    break;
                }
            }
        }
        return safeMoves;
    }

    private String filterEnPassant(String move, String currentCoords, ChessPiece.Color kingColor, ChessPiece[][] board) {
        // Pawn Move
        ChessPiece[][] hypotheticalBoard = createHypotheticalBoard(board, currentCoords, move);
        // Set the current position of the enemy pawn to null
        int[] destinationXY = coordinates.getArrayCoords(move);
        int[] currentXY = coordinates.getArrayCoords(currentCoords);
        hypotheticalBoard[currentXY[0]][destinationXY[1]] = null;

        if (!isKingInCheck(kingColor, hypotheticalBoard)) {
            return move;
        } else return null;
    }

    private String filterMove(String move, String currentCoords, ChessPiece.Color kingColor, ChessPiece[][] board) {
        ChessPiece[][] hypotheticalBoard = createHypotheticalBoard(board, currentCoords, move);
        if (!isKingInCheck(kingColor, hypotheticalBoard)) {
            return move;
        } else return null;
    }

    private String filterCastling(String move, String currentCoords, ChessPiece.Color kingColor, ChessPiece[][] board) {

        if (isKingInCheck(kingColor, board)) {
            return null;
        }

        int[] xy = coordinates.getArrayCoords(move);
        int side = (kingColor == ChessPiece.Color.WHITE) ? 7 : 0;
        // Check if the king would be in check after moving to the path square
        int pathVariant = (xy[1] == 2) ? 3 : 6;
        String pathCoords = coordinates.getChessNotationCoords(new int[]{side, pathVariant});
        ChessPiece[][] hypotheticalBoardKingPath = createHypotheticalBoard(board, currentCoords, pathCoords);
        if (isKingInCheck(kingColor, hypotheticalBoardKingPath)) {
            return null;
        }
        // Check if the king would be in check after moving to the castled square; long variant
        ChessPiece[][] hypotheticalBoardKingMove = createHypotheticalBoard(board, currentCoords, move);
        if (xy[1] == 2) {
            String rookCoords = coordinates.getChessNotationCoords(new int[]{side, 0});
            String rookDestination = coordinates.getChessNotationCoords(new int[]{side, 3});
            ChessPiece[][] hypotheticalBoardRookMove = createHypotheticalBoard(hypotheticalBoardKingMove, rookCoords, rookDestination);
            if (!isKingInCheck(kingColor, hypotheticalBoardRookMove)) {
                return move;
            }
        // short variant
        } else {
            String rookCoords = coordinates.getChessNotationCoords(new int[]{side, 7});
            String rookDestination = coordinates.getChessNotationCoords(new int[]{side, 5});
            ChessPiece[][] hypotheticalBoardRookMove = createHypotheticalBoard(hypotheticalBoardKingMove, rookCoords, rookDestination);
            if (!isKingInCheck(kingColor, hypotheticalBoardRookMove)) {
                return move;
            }
        }
        return null;
    }
}

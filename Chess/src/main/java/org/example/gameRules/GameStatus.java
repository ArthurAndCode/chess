package org.example.gameRules;

import org.example.chessman.Bishop;
import org.example.chessman.ChessPiece;
import org.example.chessman.King;
import org.example.chessman.Knight;

import java.util.ArrayList;
import java.util.List;

public class GameStatus extends Simulations {

    private boolean isCheckMate(ChessPiece.Color kingColor, ChessPiece[][] gameBoard) {
        if (!isKingInCheck(kingColor, gameBoard)) {
            return false;
        }
        ArrayList<String> safeCoords = getLegalMoves(kingColor, gameBoard);
        return safeCoords.isEmpty();
    }

    private ArrayList<String> getLegalMoves(ChessPiece.Color kingColor, ChessPiece[][] gameBoard) {
        ArrayList<String> legalMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = gameBoard[row][col];
                // Get all available moves
                if (piece != null && piece.getColor() == kingColor) {
                    String pieceCoords = coordinates.getChessNotationCoords(new int[]{row, col});
                    List<String> availableCoords = new ArrayList<>(piece.getAvailableCoordinates(pieceCoords, gameBoard));
                    legalMoves.addAll(filterSafeMoves(availableCoords, pieceCoords, kingColor, gameBoard));
                }
            }
        }
        return legalMoves;
    }

    private boolean isDeadPosition(ChessPiece[][] gameBoard) {
        ArrayList<ChessPiece> allPieces = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = gameBoard[row][col];
                if (piece != null) {
                    allPieces.add(piece);
                }
            }
        }

        int kingCount = 0;
        int bishopCount = 0;
        int knightCount = 0;
        ArrayList<Bishop> bishops = new ArrayList<>();

        for (ChessPiece piece : allPieces) {
            if (piece instanceof King) {
                kingCount++;
            } else if (piece instanceof Bishop) {
                bishopCount++;
                bishops.add((Bishop) piece);
            } else if (piece instanceof Knight) {
                knightCount++;
            }
        }
        // King vs King
        if (kingCount == 2 && allPieces.size() == 2) {
            return true;
        }
        // King and Bishop vs King
        if (kingCount == 2 && bishopCount == 1 && allPieces.size() == 3) {
            return true;
        }
        // King and Knight vs King
        if (kingCount == 2 && knightCount == 1 && allPieces.size() == 3) {
            return true;
        }
        // King and Bishop vs King and Bishop with bishops on the same color
        if (kingCount == 2 && bishopCount == 2 && allPieces.size() == 4) {
            Bishop bishop1 = bishops.get(0);
            Bishop bishop2 = bishops.get(1);
            return bishop1.getColor() == bishop2.getColor();
        }
        return false;
    }

    public String getGameStatus(ChessPiece.Color kingColor, ChessPiece[][] gameBoard) {

        if (isCheckMate(kingColor, gameBoard)) {
            return "Checkmate";
        }
        if (!isKingInCheck(kingColor, gameBoard)) {
            ArrayList<String> safeCoords = getLegalMoves(kingColor, gameBoard);
            if (safeCoords.isEmpty()) {
                return "Draw - Stalemate";
            }
        }
        if (isDeadPosition(gameBoard)) {
            return "Draw - Dead position";
        }
        return "ongoing";
    }
}

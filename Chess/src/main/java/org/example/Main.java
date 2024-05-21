package org.example;

import org.example.chessman.ChessPiece;
import org.example.gameRules.GameStatus;
import org.example.gameRules.Simulations;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ChessBoard chessBoard = new ChessBoard();
    private static final Simulations simulation = new Simulations();
    private static final GameStatus gameStatus = new GameStatus();

    public static void main(String[] args) {
        boolean whiteTurn = true;

        while (isGameOngoing(whiteTurn)) {
            printBoardAndPromptPlayer(whiteTurn);

            String currentCoords = getInput("Pick your chess piece:");

            if (isValidPieceSelection(currentCoords, whiteTurn)) {
                List<String> safeCoords = getSafeMoves(currentCoords, whiteTurn);

                printAvailableMoves(safeCoords);

                String destinationCoords = getInput("Choose destination:");

                if (safeCoords.contains(destinationCoords)) {
                    chessBoard.makeMove(currentCoords, destinationCoords);
                    whiteTurn = !whiteTurn;
                } else {
                    System.out.println("Invalid move, please choose again.");
                }
            } else {
                System.out.println("Invalid piece selection, please choose again.");
            }
        }

        printGameResult(whiteTurn);
    }

    // Game utilities located under
    private static boolean isGameOngoing(boolean whiteTurn) {
        return gameStatus.getGameStatus(whiteTurn ?
                ChessPiece.Color.WHITE : ChessPiece.Color.BLACK, chessBoard.getGameBoard()).equals("ongoing");
    }

    private static void printBoardAndPromptPlayer(boolean whiteTurn) {
        chessBoard.printChessBoard();
        String currentPlayer = whiteTurn ? "White" : "Black";
        System.out.println(currentPlayer + " turn.");
    }

    private static String getInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    private static boolean isValidPieceSelection(String currentCoords, boolean whiteTurn) {
        return chessBoard.isAccessible(currentCoords, whiteTurn) && currentCoords.matches("[a-h][1-8]");
    }

    private static List<String> getSafeMoves(String currentCoords, boolean whiteTurn) {
        List<String> availableCoords = chessBoard.getAvailableCoordsForChessPiece(currentCoords);
        return simulation.filterSafeMoves(availableCoords, currentCoords,
                whiteTurn ? ChessPiece.Color.WHITE : ChessPiece.Color.BLACK, chessBoard.getGameBoard());
    }

    private static void printAvailableMoves(List<String> safeCoords) {
        System.out.println("Available coordinates: ");
        System.out.println(safeCoords);
    }

    private static void printGameResult(boolean whiteTurn) {
        String result = gameStatus.getGameStatus(whiteTurn ?
                ChessPiece.Color.WHITE : ChessPiece.Color.BLACK, chessBoard.getGameBoard());

        if (result.equals("Checkmate")) {
            String winner = whiteTurn ? "Black" : "White";
            System.out.println(result + "! " + winner + " wins.");
        } else {
            System.out.println(result);
        }
    }
}
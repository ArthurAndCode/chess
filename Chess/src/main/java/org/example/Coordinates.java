package org.example;

public class Coordinates {
    private static final String[][] board = new String[8][8];
    public Coordinates() {
        initializeCoords();
    }
    private void initializeCoords() {
        for (int row=0; row<8; row++) {
            for (int col=0; col<8; col++) {
                board[row][col] = String.format("%s%s", convertLet(col+1), convertInt(row) );
            }
        }
    }
    public int[] getArrayCoords(String chessNotation) {
        int x = 0;
        int y = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (chessNotation.contains(board[row][col])) {
                    x = row;
                    y = col;
                }
            }
        }
        return new int[]{x, y};
    }
    public String getChessNotationCoords(int[] xy) {
        return String.format("%s%s", convertLet(xy[1]+1), convertInt(xy[0]));
    }
    private char convertLet(int num) {
        return (char)(num + 96);
    }
    private int convertInt(int num) {
        return 8 - num;
    }


}

package battleship;

import java.util.Map;

public class Player {

    private int id;
    private static int counter = 1;
    private char[][] board;

    public Player() {
        this.id = counter;
        counter++;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

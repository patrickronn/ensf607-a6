package exercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class represents a player who can play a game.
 *
 * A Player plays a Game and can view the playing Board. They also keep track of an opponent
 * Player and the Referee.
 *
 * @author Patrick Ronn Linang
 * @since September 27, 2020
 */
public class Player {
    /**
     * Represents name of the player.
     */
    private String name;

    /**
     * Represents the playing board.
     */
    private Board board;

    /**
     * Represents the other, second player.
     */
    private Player opponent;

    /**
     * The mark used to represent the player on the board.
     */
    private char mark;

    /**
     * Constructs a player with their specified name and mark.
     *
     * A new player isn't assigned to a board yet.
     * @param name name of the player
     * @param mark a char representing the player's mark on the board
     */
    public Player(String name, char mark) {
        this.name = name;
        this.mark = mark;
        setBoard(null);
    }

    /**
     * Plays the game between the player invoking this method and their assigned opponent.
     *
     * Assumes that the player invoking this method has the starting move.
     * Displays winner (or announces a tie) once game is over to std out.
     *
     * @throws IOException error if there's issues reading a player's move
     */
    public void play() throws IOException {
        // Players keep making moves until someone wins or board is full
        Player playerCurrentTurn;
        Player playerNextTurn = this;

        do {
            playerCurrentTurn = playerNextTurn;

            // Show board
            board.display();

            // Player makes a move during their turn
            playerCurrentTurn.makeMove();

            // Update whose turn is next
            if (playerNextTurn == this)
                playerNextTurn = opponent;
            else
                playerNextTurn = this;
        } while (!board.xWins() && !board.oWins() && !board.isFull());

        // Display winner to std out (logically, the winner is the player who ended the iteration above)
        if (board.xWins()) {
            board.display();
            System.out.println("THE GAME IS OVER: " + playerCurrentTurn.name + " is the winner!");
        }
        else if (board.oWins()) {
            board.display();
            System.out.println("THE GAME IS OVER: " + playerCurrentTurn.name + " is the winner!");
        }
        else { // if (board.isFull())
            board.display();
            System.out.println("THE GAME IS OVER: there is no winner; it's a tie!");
        }
    }

    /**
     * Represents a player move by asking player to place their mark on a specific tile.
     *
     * @throws IOException error if there's issues reading from std in
     */
    public void makeMove() throws IOException {
        // Use a buffered reader to get player input from std in
        BufferedReader stdin;
        stdin = new BufferedReader(new InputStreamReader(System.in));

        // Get index values from player
        int row, col;
        do {
            row = getRowIndex(stdin);
            col = getColumnIndex(stdin);
        } while (!isValidMove(row, col));

        // Update board with player mark
        board.addMark(row, col, this.mark);
    }

    /**
     * A helper method to check if a user selected valid index values for their next move.
     *
     * @param row the row index to add a mark
     * @param col the column index to add a mark
     * @return true if index is valid; else false if index values are out of range or the tile is already marked.
     */
    private boolean isValidMove(int row, int col) {
        // Check that indices are within bounds
        if (row > 2 || row < 0 || col > 2 || col < 0) {
            System.out.println("Indices are out of range (choose index values between 0-2).");
            return false;
        }
        // Check that the tile is empty
        else if (board.getMark(row, col) == this.mark || board.getMark(row, col) == opponent.mark) {
            System.out.println("A mark was already placed at (" + row + ", " + col + "). Choose another spot.");
            return false;
        }
        else
            return true;
    }

    /**
     * Helper method to get row index from std in when making a move.
     *
     * @param stdin a BufferedReader to std in
     * @return the row index to add a mark
     * @throws IOException error if there's issues reading from std in
     */
    private int getRowIndex(BufferedReader stdin) throws IOException {
        // Get row index from player
        System.out.print(this.name + ", what row should your next " + this.mark + " be placed in? ");
        int row;
        while (true) {
            try {
                row = Integer.parseInt(stdin.readLine());
                break;
            }
            catch (NumberFormatException e) {
                System.out.print("Invalid index, please try again (row index 0-2): ");
            }
        }

        return row;
    }

    /**
     * Helper method to get column index from std in when making a move.
     *
     * @param stdin a BufferedReader to std in
     * @return the column index to add a mark
     * @throws IOException error if there's issues reading from std in
     */
    private int getColumnIndex(BufferedReader stdin) throws IOException {
        // Get column index from player
        System.out.print(this.name + ", what column should your next " + this.mark + " be placed in? ");
        int col;
        while (true) {
            try {
                col = Integer.parseInt(stdin.readLine());
                break;
            }
            catch (NumberFormatException e) {
                System.out.print("Invalid index, please try again (column index 0-2): ");
            }
        }

        return col;
    }

    /**
     * Setter method to assign the player's opponent.
     *
     * @param opponent reference to the opposing Player
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * Setter method to specify which board to play on.
     *
     * @param theBoard reference to the Board object
     */
    public void setBoard(Board theBoard) {
        this.board = theBoard;
    }
}

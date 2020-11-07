package exercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class represents a player who can play a game.
 *
 * A Player plays a Game and can view the playing Board. They also keep track of an opponent
 * Player and the Referee.
 *
 * Initializes with reference to player client via socket connection.
 *
 * @author Patrick Ronn Linang
 * @since November 1, 2020
 */
public class Player {

    /**
     * The socket connecting to the player's client.
     */
    private Socket playerSocket;

    /**
     * Reads from a client.
     */
    private BufferedReader socketIn;

    /**
     * Writes to a client.
     */
    private PrintWriter socketOut;

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
     * Flag as to whether the player client contains a GUI.
     */
    private boolean playerGUIConnected;

    /**
     * Constructs a player with their specified name and mark.
     *
     * A new player isn't assigned to a board yet.
     * @param name name of the player
     * @param mark a char representing the player's mark on the board
     * @param playerSocket connected
     * @param playerGUIConnected true if player client contains a GUI; otherwise, false.
     */
    public Player(String name, char mark, Socket playerSocket, boolean playerGUIConnected) {
        try {
            // Initialize member variables
            this.name = name;
            this.mark = mark;
            this.playerSocket = playerSocket;
            this.socketIn = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            this.socketOut = new PrintWriter(playerSocket.getOutputStream(), true);
            this.playerGUIConnected = playerGUIConnected;

            setBoard(null);

            // If GUI is connected, notify GUI of the player username and mark
            if (playerGUIConnected)
                sendUserInfo();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends the player's username and assigned mark to the player client.
     */
    public void sendUserInfo() {
        socketOut.println("Set username:");
        socketOut.println(name);
        socketOut.println("Set mark:");
        socketOut.println(mark);
    }

    /**
     * Plays the game between the player invoking this method and their assigned opponent.
     *
     * Assumes that the player invoking this method has the starting move.
     * Displays winner (or announces a tie) once game is over to std out.
     */
    public void play() throws IOException {
        // Players keep making moves until someone wins or board is full
        Player playerCurrentTurn = opponent;
        Player playerNextTurn = this;

        do {
            // Show board to both players
            playerNextTurn.displayBoard();
            playerCurrentTurn.displayBoard();

            playerCurrentTurn.socketOut.println("Waiting on opponent's turn...");

            // Switch turn, display, and make move
            playerCurrentTurn = playerNextTurn;
            playerCurrentTurn.displayBoard();
            playerCurrentTurn.makeMove();

            // Update whose turn is next
            if (playerNextTurn == this)
                playerNextTurn = opponent;
            else
                playerNextTurn = this;
        } while (!board.xWins() && !board.oWins() && !board.isFull());

        // Reach here when game is finished:
        this.displayBoard();
        opponent.displayBoard();

        String gameOverString;
        // Display winner (logically, the winner is the player whose turn broke the play loop)
        if (board.xWins() || board.oWins())
            gameOverString = "THE GAME IS OVER: " + playerCurrentTurn.name + " is the winner!";
        else // if (board.isFull())
            gameOverString = "THE GAME IS OVER: there is no winner; it's a tie!";

        this.socketOut.println(gameOverString);
        opponent.socketOut.println(gameOverString);
    }

    /**
     * Represents a player move by asking player to place their mark on a specific tile.
     */
    public void makeMove() throws IOException {
        // Get index values from player
        int row, col;
        do {
            row = getRowIndex();
            col = getColumnIndex();
        } while (!isValidMove(row, col));

        // Update board with player mark
        board.addMark(row, col, this.mark);
    }

    public void displayBoard() {
        if (playerGUIConnected)
            board.sendTileValues(this.socketOut);
        else
            board.display(this.socketOut);
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
            socketOut.println("Indices are out of range (choose index values between 0-2).");
            return false;
        }
        // Check that the tile is empty
        else if (board.getMark(row, col) == this.mark || board.getMark(row, col) == opponent.mark) {
            socketOut.println("A mark was already placed at (" + row + ", " + col + "). Choose another spot.");
            return false;
        }
        else
            return true;
    }

    /**
     * Helper method to get row index from std in when making a move.
     *
     * @return the row index to add a mark
     */
    private int getRowIndex() throws IOException {
        // Get row index from player
        socketOut.println(this.name + ", what row should your next " + this.mark + " be placed in? ");
        int row;
        while (true) {
            try {
                String line = socketIn.readLine();
                if (line == null)
                    throw new IOException("a PlayerClient disconnected");
                row = Integer.parseInt(line);
                break;
            }
            catch (NumberFormatException e) {
                socketOut.println("Invalid index - Please try again (row index 0-2): ");
            }
        }

        return row;
    }

    /**
     * Helper method to get column index from std in when making a move.
     *
     * @return the column index to add a mark
     */
    private int getColumnIndex() throws IOException {
        // Get column index from player
        socketOut.println(this.name + ", what column should your next " + this.mark + " be placed in? ");
        int col;
        while (true) {
            try {
                String line = socketIn.readLine();
                if (line == null)
                    throw new IOException("a PlayerClient disconnected");
                col = Integer.parseInt(line);
                break;
            }
            catch (NumberFormatException e) {
                socketOut.println("Invalid index - Please try again (column index 0-2): ");
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

    /**
     * Close IO connection points for IO and socket connection.
     * @throws IOException error when attempting to close
     */
    public void close() throws IOException {
        if (socketIn != null)
            socketIn.close();
        if (socketOut != null)
            socketOut.close();
        if (playerSocket != null)
            playerSocket.close();
    }
}

package exercise4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerClient {
    /**
     * Connects to a TicTacToeServer.
     */
    private Socket gameSocket;

    /**
     * Read user input from std in.
     */
    private BufferedReader stdIn;

    /**
     * Reads from a socket.
     */
    private BufferedReader socketIn;

    /**
     * Writes to a socket.
     */
    private PrintWriter socketOut;

    /**
     * FOR EXERCISE 5: Reference to player's graphical user interface
     */
    PlayerGUI playerGUI;

    /**
     * FOR EXERCISE 5: Controls whether a user's input to the GUI will be read.
     */
    boolean allowMoveSelection;

    /**
     * Instantiates a client connected to a specified port.
     * @param servername name of the server (e.g. "localhost")
     * @param portNumber name of port to connect (e.g. 9090)
     */
    public PlayerClient(String servername, int portNumber) {
        try {
            // Set up socket and streams
            gameSocket = new Socket(servername, portNumber);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socketIn = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            socketOut = new PrintWriter(gameSocket.getOutputStream(), true);
            playerGUI = null;
            allowMoveSelection = false;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Instantiates a client connected to a specified port and a GUI.
     *
     * Adds event listeners to the GUI's board.
     *
     * @param servername name of the server (e.g. "localhost")
     * @param portNumber name of port to connect (e.g. 9090)
     * @param playerGUI  a PlayerGUI for user IO
     */
    public PlayerClient(String servername, int portNumber, PlayerGUI playerGUI) {
        this(servername, portNumber);
        this.playerGUI = playerGUI;

        // Add ActionListeners for each tile
        JButton[][] tiles = playerGUI.getTiles();
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[i].length; j++)
                    tiles[i][j].addActionListener(new TileButtonListener(i, j));
    }

    /**
     * Connects the client to a game and handles IO with command line and the server.
     *
     * Handles different server messages accordingly.
     */
    public void connectToGame() {
        String serverMessage;

        // Notify server of the client interface type
        if (playerGUI == null)
            socketOut.println("CLUI");
        else
            socketOut.println("GUI");

        boolean gameRunning = true;

        // Keeps running game until a game over condition is reached.
        while (gameRunning) {
            try {
                // Wait for a message from server
                serverMessage = socketIn.readLine();

                // If server is unresponsive terminate
                if (serverMessage == null) {
                    displayMessage("Error: connection with server was lost.");
                    gameRunning = false;
                }
                // Stop if game is finished running
                else if (serverMessage.contains("THE GAME IS OVER")) {
                    // Print message to std out
                    displayMessage(serverMessage);
                    gameRunning = false;
                }
                else if (serverMessage.toLowerCase().contains("enter your name")) {
                    String line = getUserInput(serverMessage);
                    socketOut.println(line);

                }
                // Search for phrases that require user input and send to server
                else if (serverMessage.toLowerCase().contains("what column") ||
                         serverMessage.toLowerCase().contains("what row") ||
                        serverMessage.toLowerCase().contains("invalid index")) {
                    allowMoveSelection = true;
                    if (playerGUI == null) {
                        String line = getUserInput(serverMessage);
                        socketOut.println(line);
                    }
                    else { // playerGUI == true
                        displayMessage(serverMessage);
                    }
                }
                // Search for 'Draw:' to begin update GUI tile values
                else if (serverMessage.contains("Draw:")) {
                    if (playerGUI == null) {
                        displayMessage("Error: no GUI was connected");
                        System.exit(1);
                    }
                    else {
                        // Read stream of tile values
                        char[][] updatedTiles = new char[3][3];
                        for (int i = 0; i < 3; i++)
                            for (int j = 0; j < 3; j++)
                                updatedTiles[i][j] = socketIn.readLine().charAt(0);
                        // Update GUI
                        playerGUI.updateTileValues(updatedTiles);
                    }
                }
                // Search for 'Set username:' to update username
                else if (serverMessage.contains("Set username:")) {
                    if (playerGUI == null) {
                        displayMessage("Error: no GUI was connected");
                        System.exit(1);
                    }
                    else {
                        // Read username then set it in GUI
                        String username = socketIn.readLine();
                        playerGUI.setPlayerUsername(username);
                    }
                }
                // Search for 'Set mark:' to update player mark
                else if (serverMessage.contains("Set mark:")) {
                    if (playerGUI == null) {
                        displayMessage("Error: no GUI was connected");
                        System.exit(1);
                    }
                    else {
                        // Read username then set it in GUI
                        String markStr = socketIn.readLine();
                        playerGUI.setPlayerMark(markStr.charAt(0));
                    }
                }
                else {
                    // Print message to std out
                    displayMessage(serverMessage);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Close IO connection points
        try {
            stdIn.close();
            socketIn.close();
            gameSocket.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        socketOut.close();
    }

    /**
     * Displays a message to the user, depending on whether the client is CLUI or GUI.
     * @param message the String message to display
     */
    public void displayMessage(String message) {
        if (playerGUI == null)
            System.out.println(message);
        else
            playerGUI.appendServerMessageText("\n" + message);
    }

    /**
     * Displays a prompt and retrieves user input based on the prompt.
     *
     * Works for both CLUI and GUI.
     *
     * @param prompt the String message to display
     * @return String user input based on the prompt
     * @throws IOException error if there's issues reading from stdIn
     */
    public String getUserInput(String prompt) throws IOException {
        String line;
        if (playerGUI == null) {
            displayMessage(prompt);
            line = stdIn.readLine();
                while (line == null || line.isEmpty()) {
                    displayMessage("Invalid input, please try again:");
                    line = stdIn.readLine();
                }
        }
        else {
            line = playerGUI.getUserInput(prompt);
            while (line == null || line.isEmpty()) {
                displayMessage("Invalid input, please try again:");
                line = playerGUI.getUserInput(prompt);
            }
        }
        return line;
    }

    /**
     * Provides an event listener for button clicks on the board tiles
     */
    class TileButtonListener implements ActionListener {

        /**
         * Keeps track of the row and column index values to its attached button.
         */
        private int rowIdx, colIdx;

        /**
         * Row and column indices (which are zero-indexed).
         * @param i row index
         * @param j column index
         */
        public TileButtonListener(int i, int j) {
            rowIdx = i;
            colIdx = j;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Send server the row and col indices
            if (allowMoveSelection) {
                allowMoveSelection = false;
                socketOut.println(rowIdx);
                socketOut.println(colIdx);
            }
        }
    }

    public static void main(String[] args) {
//        PlayerClient player = new PlayerClient("localhost", 8099);
        PlayerClient player = new PlayerClient("localhost", 8099, new PlayerGUI("Tic Tac Toe Player"));
        player.connectToGame();

    }
}

package exercise4;

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
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Connects the client to a game and handles IO with command line and the server.
     */
    public void connectToGame() {
        String serverMessage;
        String line = "";
        boolean gameRunning = true;
        while (gameRunning) {
            try {
                // Wait for a message from server
                serverMessage = socketIn.readLine();

                // Print message to std out
                System.out.println(serverMessage);

                // If server is unresponsive terminate
                if (serverMessage == null) {
                    System.err.println("Error: connection with server was lost.");
                    gameRunning = false;
                }
                // Search for phrases that require user input and send to server
                else if (serverMessage.toLowerCase().contains("enter your name") ||
                         serverMessage.toLowerCase().contains("try again") ||
                         serverMessage.contains("?")) {

                    line = stdIn.readLine();

                    while (line == null || line.isEmpty()) {
                        System.out.println("Invalid input, please try again:");
                        line = stdIn.readLine();
                    }
                    socketOut.println(line);
                }
                // Stop if game is finished running
                else if (serverMessage.contains("THE GAME IS OVER")) {
                    gameRunning = false;
                }
                else {
                    System.out.println("Error: cannot interpret message from server.");
                    System.out.println("Server message: " + serverMessage);
                    gameRunning = false;
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

    public static void main(String[] args) {
        PlayerClient player = new PlayerClient("localhost", 8099);
        player.connectToGame();
    }
}

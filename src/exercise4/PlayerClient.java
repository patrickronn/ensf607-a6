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
        }
    }

    /**
     * Connects the client to a game and handles IO with command line and the server.
     */
    public void connectToGame() {
        try {
            String response = "";
            boolean gameRunning = true;
            while (gameRunning) {
                // Wait for a response from server
                response = socketIn.readLine();

                // If response doesn't contain "Message:", assume it requires a response
                if (!response.contains("Message:")) {
                    // Print response to std out
                    System.out.println(response);

                    // Read user input and send to server
                    socketOut.println(stdIn.readLine());
                }
                else {
                    // Print response to std out
                    System.out.println(response);
                }

                // Stop if game is finished running
                if (response.equals("Message: Game finished.")) {
                    gameRunning = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close streams
            try {
                stdIn.close();
                socketIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socketOut.close();
        }
    }

    public static void main(String[] args) {
        PlayerClient player = new PlayerClient("localhost", 9090);
        player.connectToGame();
    }
}

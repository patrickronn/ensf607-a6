package exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creates a Server for Exercise 1 for checking client inputs for palindromes.
 *
 * @author Patrick Linang
 * @since November 1, 2020
 */
public class Server {
    /**
     * Waits for requests to come in through the network.
     */
    private ServerSocket serverSocket;

    /**
     * Used to accept client connection.
     */
    private Socket aSocket;

    /**
     * Writes to a socket.
     */
    private PrintWriter socketOut;

    /**
     * Reads from a socket.
     */
    private BufferedReader socketIn;

    /**
     * Instantiates a server with an initialized server socket to specified port.
     * @param portNumber name of port to bind (e.g. 8099)
     */
    public Server(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is now running.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the server and check if any client input strings are palindromes.
     */
    public void runServer() {
        try {
            while (true) {
                // Wait for a client connection to accept
                aSocket = serverSocket.accept();

                System.out.println("Server accepted a clinent.");

                // Open IO streams
                socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
                socketOut = new PrintWriter(aSocket.getOutputStream(), true);

                // Read user input to check if palindrome
                checkPalindrome();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close streams
            try {
                socketIn.close();
                serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            socketOut.close();
        }
    }

    /**
     * Reads a word from socketIn and outputs to socketOut if the word is a palindrome.
     */
    public void checkPalindrome() {
        try {
            while (true) {
                // Read word from incoming client
                String word = socketIn.readLine();

                // Reverse the word using StringBuilder
                StringBuilder reverseWordBuilder = new StringBuilder(word);
                reverseWordBuilder.reverse();

                // Check if the word is a palindrome, then return statement back to client
                if (word.toLowerCase().equals(reverseWordBuilder.toString().toLowerCase()))
                    socketOut.println(word + " is a Palindrome.");
                else
                    socketOut.println(word + " is not a Palindrome.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server myServer = new Server(8099);
        myServer.runServer();
    }
}

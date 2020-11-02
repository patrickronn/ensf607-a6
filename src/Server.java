import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
     * @param serverName name of the server (e.g. "localhost")
     * @param portNumber name of port to bind (e.g. 8989)
     */
    public Server(String serverName, int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
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

                System.out.println("Server is now running.");

                // Open IO streams
                socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
                socketOut = new PrintWriter(aSocket.getOutputStream(), true);

                checkPalindrome();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            // Close sockets
            try {
                socketIn.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            socketOut.close();
        }
    }

    public void checkPalindrome() {
        try {
            // Read word from incoming client
            String word = socketIn.readLine();

            // Reverse the word using StringBuilder
            StringBuilder reverseWordBuilder = new StringBuilder(word.toLowerCase());
            reverseWordBuilder.reverse();

            // Check if the word is a palindrome, then return statement back to client
            if (word.toLowerCase().equals(reverseWordBuilder.toString()))
                socketOut.println(word + "is not a Palindrome.");
            else
                socketOut.println(word + "is a Palindrome.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server myServer = new Server("localhost", 8989);
        myServer.runServer();
    }
}

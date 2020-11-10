package exercise2;

import java.io.*;
import java.net.Socket;

public class Client {
    /**
     * Connects to a DateServer.
     */
    private Socket dateSocket;

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
     * Instantiates a Client connected to a specified port.
     * @param servername name of the server (e.g. "localhost")
     * @param portNumber name of port to connect (e.g. 9090)
     */
    public Client(String servername, int portNumber) {
        try {
            // Set up socket and streams
            dateSocket = new Socket(servername, portNumber);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            socketIn = new BufferedReader(new InputStreamReader(dateSocket.getInputStream()));
            socketOut = new PrintWriter(dateSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a user option from std in and sends the option to the server.
     */
    public void communicate() {
        try {
            String response = "";
            while (true) {
                // Prompt for user input
                System.out.println("Please select an option (DATE/TIME)");

                // Get an option selected by user and send to server
                socketOut.println(stdIn.readLine());

                // Get response from server and print to std out
                response = socketIn.readLine();
                System.out.println(response);
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
        Client aClient = new Client("localhost", 9090);
        aClient.communicate();
    }
}

package exercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer implements Constants {
    /**
     * Waits for requests to come in through the network.
     */
    private ServerSocket serverSocket;

    /**
     * Used to accept client connection.
     */
    private Socket aSocket;

    /**
     * Writes to a client socket.
     */
    private PrintWriter socketOut;

    /**
     * Reads from a client socket.
     */
    private BufferedReader socketIn;

    /**
     * Thread pool for running multiple games in parallel.
     */
    private ExecutorService gamePool;

    /**
     * Controls the maximum # of games that can run in parallel.
     */
    private final static int MAX_CONCURRENT_GAMES = 2;

    /**
     * Temporarily stores reference to a player who's waiting for an opponent.
     */
    private Player xPlayerWaiting;

    /**
     * Instantiates a server to a specified port and a thread pool.
     * @param portNumber name of port to bind (e.g. 8099)
     */
    public GameServer(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            gamePool = Executors.newFixedThreadPool(MAX_CONCURRENT_GAMES);
            xPlayerWaiting = null;
            System.out.println("Server is now running.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() {
        try {
            String name = "";
            while (true) {
                // Wait for a client connection
                aSocket = serverSocket.accept();
                System.out.println("Server: a new player client has connected.");

                // Open IO streams
                socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
                socketOut = new PrintWriter(aSocket.getOutputStream(), true);

                socketOut.println("Message: WELCOME TO THE GAME");

                // Handle whether the new client is first player (xPlayer) or second player (oPlayer)
                if (xPlayerWaiting == null) {
                    // Prompt for first player's name
                    socketOut.println("Please enter the name of the 'X' player: ");

                    // Create a new player and notify them to wait for opponent
                    name = socketIn.readLine();
                    xPlayerWaiting = new Player(name, LETTER_X, socketIn, socketOut);

                    socketOut.println("Message: Waiting for opponent to connect");
                }
                else {
                    // Prompt for second player's name
                    socketOut.println("Please enter the name of the 'O' player: ");

                    // Create a new player
                    name = socketIn.readLine();
                    Player oPlayer = new Player(name, LETTER_O, socketIn, socketOut);

                    // Start a new game
                    startNewGame(xPlayerWaiting, oPlayer);
                    xPlayerWaiting = null;
                }
            }
        } catch (IOException e) {
            System.out.println("Server error occurred:");
            e.printStackTrace();
        } finally {
            // Close streams
            try {
                socketIn.close();
                serverSocket.close();
            }
            catch (IOException e) {
                System.out.println("Error while closing streams:");
                e.printStackTrace();
            }
            socketOut.close();
        }
    }

    public void startNewGame(Player xPlayer, Player oPlayer) throws IOException {
        // Instantiate game and referee
		Game theGame = new Game();
		Referee theRef = new Referee();

		// Assign board to players
        xPlayer.setBoard(theGame.getTheBoard());
        oPlayer.setBoard(theGame.getTheBoard());

        // Assign game board and players to referee
		theRef.setBoard(theGame.getTheBoard());
		theRef.setoPlayer(oPlayer);
		theRef.setxPlayer(xPlayer);

		// Assign referee to the game
        theGame.appointReferee(theRef);

        // Start the game
        gamePool.execute(theGame);
    }

    public static void main(String[] args) {
        GameServer myGameServer = new GameServer(8099);
        myGameServer.runServer();
    }
}

package exercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer implements Constants {
    /**
     * Waits for requests to come in through the network.
     */
    private ServerSocket serverSocket;

    /**
     * Used to accept client connection.
     */
    private Socket socket;

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

    /**
     * Waits for incoming client connections and assigns them as Players.
     * Starts a new game if two Player clients are ready.
     */
    public void runServer() {
        try {
            while (true) {
                // Wait for a client connection
                socket = serverSocket.accept();
                System.out.println("Server: a new player client has connected on " + socket.getLocalSocketAddress());

                // Create IO connections
                socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                socketOut = new PrintWriter(socket.getOutputStream(), true);

                socketOut.println("Message: WELCOME TO THE GAME");
                socketOut.println("Please enter your name: ");
                String name = socketIn.readLine();

                // Handle whether the new client is first player (xPlayer) or second player (oPlayer)
                if (xPlayerWaiting == null && name != null) {
                    // Create Player 'X'
                    socketOut.println(name + ", you are assigned to Player 'X'.");
                    xPlayerWaiting = new Player(name, LETTER_X, socket);

                    socketOut.println("\nMessage: Waiting for an opponent to connect...");
                }
                else if (xPlayerWaiting != null && name != null){
                    // Create Player 'O'
                    socketOut.println(name + ", you are assigned to Player 'O'.");
                    Player oPlayer = new Player(name, LETTER_O, socket);

                    // Start a new game since two players are ready
                    startNewGame(xPlayerWaiting, oPlayer);
                    xPlayerWaiting = null;  // reset so server can accept the next client as xPlayerWaiting
                }
                else { // name == null
                    System.out.println("Server: lost connection with player client; waiting for a new connection.");
                    socket.close();
                    socketIn.close();
                    socketOut.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Server: IO error occurred with new client connection");
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown thread pool
            gamePool.shutdown();
            System.out.println("Server is now shutting down.");
            try {
                // Wait for threads to finish
                gamePool.awaitTermination(1,  TimeUnit.HOURS);

                // Close streams
                // Note: Player close() method handles closing connections to PlayerClients
                socketIn.close();
                serverSocket.close();
                socketOut.close();
            }
            catch (InterruptedException e) {
                System.out.println("Server: a thread was interrupted");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e) {
                System.out.println("Server: error while closing streams");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a game on new thread whenever two Players are available.
     *
     * @param xPlayer player with mark 'X' who will make the first move
     * @param oPlayer player with mark '0' who is the other opponent
     */
    public void startNewGame(Player xPlayer, Player oPlayer) {
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

        // Start the game on a thread
        gamePool.execute(theGame);
    }

    public static void main(String[] args) {
        GameServer myGameServer = new GameServer(8099);
        myGameServer.runServer();
    }
}

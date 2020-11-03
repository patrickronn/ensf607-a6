package exercise4;


import java.io.IOException;

/**
 * This class represents a board game of tic-tac-toe with a referee and played by two players.
 *
 * Includes the program's main method.
 *
 * @author Patrick Ronn Linang
 * @since November 1, 2020
 */
public class Game implements Constants, Runnable {
	/**
	 * Represents the game's playing board.
	 */
	private Board theBoard;

	/**
	 * Represents the game's referee.
	 */
	private Referee theRef;

	/**
	 * Constructs Game with a new, empty playing board.
	 */
    public Game() {
        theBoard  = new Board();
	}

	/**
	 * Assigns a referee to the game.
	 *
	 * @param r reference to a Referee object
	 */
    public void appointReferee(Referee r) {
        theRef = r;
    }

	/**
	 * Getter method for theBoard.
	 * @return reference to the game's board
	 */
	public Board getTheBoard() {
		return theBoard;
	}

	/**
	 * Runs a game managed by a thread.
	 */
	@Override
	public void run() {
		try {
			System.out.println("Server: a new game has started on " + Thread.currentThread().getName() + ".");
			theRef.runTheGame();
		} catch (IOException e) {
			System.out.println("Server: error while running game. ");
			e.printStackTrace();
		}
		finally {
			System.out.println("Server: a game has ended on " + Thread.currentThread().getName() + ".");
			try {
				// End game by properly closing IO streams and sockets to player clients
				theRef.endTheGame();
			} catch (IOException e) {
				System.out.println("Server: error when closing player IO connection.");
				e.printStackTrace();
			}
		}
	}
}

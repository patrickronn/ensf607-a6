package exercise4;

import java.io.*;

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
    public Game( ) {
        theBoard  = new Board();
	}

	/**
	 * Assigns a referee to the game.
	 *
	 * @param r reference to a Referee object
	 * @throws IOException error if there's issues reading a player's move
	 */
    public void appointReferee(Referee r) throws IOException {
        theRef = r;
    	theRef.runTheGame();
    }

	/**
	 * Getter method for theBoard.
	 * @return reference to the game's board
	 */
	public Board getTheBoard() {
		return theBoard;
	}

	@Override
	public void run() {
    	theRef = new Referee();
    	theRef.setBoard(this.theBoard);
	}
}

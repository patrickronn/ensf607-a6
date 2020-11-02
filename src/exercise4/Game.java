package exercise4;

import java.io.*;

/**
 * This class represents a board game of tic-tac-toe with a referee and played by two players.
 *
 * Includes the program's main method.
 *
 * @author Patrick Ronn Linang
 * @since September 27, 2020
 */
public class Game implements Constants {
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
	 * Plays a game of tic-tac-toe using command line as the interface.
	 * @param args not used
	 * @throws IOException error if there's issues reading a player's move
	 */
	public static void main(String[] args) throws IOException {
		Referee theRef;
		Player xPlayer, oPlayer;
		BufferedReader stdin;
		Game theGame = new Game();
		stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("\nPlease enter the name of the \'X\' player: ");
		String name= stdin.readLine();
		while (name == null) {
			System.out.print("Please try again: ");
			name = stdin.readLine();
		}

		xPlayer = new Player(name, LETTER_X);
		xPlayer.setBoard(theGame.theBoard);
		
		System.out.print("\nPlease enter the name of the \'O\' player: ");
		name = stdin.readLine();
		while (name == null) {
			System.out.print("Please try again: ");
			name = stdin.readLine();
		}
		
		oPlayer = new Player(name, LETTER_O);
		oPlayer.setBoard(theGame.theBoard);
		
		theRef = new Referee();
		theRef.setBoard(theGame.theBoard);
		theRef.setoPlayer(oPlayer);
		theRef.setxPlayer(xPlayer);
        
        theGame.appointReferee(theRef);
	}
	

}

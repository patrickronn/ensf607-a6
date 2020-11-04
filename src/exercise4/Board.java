package exercise4;


import java.io.PrintWriter;

/**
 * This class represents a 3x3 playing board where each tile gets marked.
 *
 * Includes methods to determine if a winner exists and to display the board to std out.
 *
 * @author Patrick Ronn Linang
 * @since November 1, 2020
 */
public class Board implements Constants {
	/**
	 * Stores the state of each tile on the playing board.
	 */
	private char theBoard[][];

	/**
	 * Tracks the count of filled-in tiles.
	 */
	private int markCount;

	/**
	 * Instantiates a new 3x3 board to an empty state.
	 */
	public Board() {
		markCount = 0;
		theBoard = new char[3][];
		for (int i = 0; i < 3; i++) {
			theBoard[i] = new char[3];
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		}
	}

	/**
	 * Getter method to check who marked a specified board tile (or check if it's empty).
	 *
	 * @param row row index of the tile
	 * @param col column index of the tile
	 * @return a char representing the state of the tile (SPACE_CHAR, LETTER_X, or LETTER_X)
	 */
	public char getMark(int row, int col) {
		return theBoard[row][col];
	}

	/**
	 * Check if the board is completely filled
	 * @return true if board is full; else false.
	 */
	public boolean isFull() {
		return markCount == 9;
	}

	/**
	 * Checks whether player assigned to 'X' has won.
	 *
	 * @return true if 'X' player won; else false.
	 */
	public boolean xWins() {
		if (checkWinner(LETTER_X) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Checks whether player assigned to 'O' has won.
	 *
	 * @return true if 'O' player won; else false.
	 */
	public boolean oWins() {
		if (checkWinner(LETTER_O) == 1)
			return true;
		else
			return false;
	}

	/**
	 * Writes a text-block version of board to a socket.
	 * Used to visualize how each tile was marked (and which ones are empty).
	 */
	public void display(PrintWriter socketOut) {
		displayColumnHeaders(socketOut);
		addHyphens(socketOut);
		for (int row = 0; row < 3; row++) {
			addSpaces(socketOut);
			socketOut.print("    row " + row + ' ');
			for (int col = 0; col < 3; col++)
				socketOut.print("|  " + getMark(row, col) + "  ");
			socketOut.println("|");
			addSpaces(socketOut);
			addHyphens(socketOut);
		}
	}

	/**
	 * Places a mark on the board and updates mark count.
	 *
	 * @param row row index of the tile
	 * @param col column index of the tile
	 * @param mark the mark to place (usually either LETTER_O or LETTER_X)
	 */
	public void addMark(int row, int col, char mark) {
		
		theBoard[row][col] = mark;
		markCount++;
	}

	/**
	 * Resets the state of the board by making all tiles empty and resetting mark count
	 */
	public void clear() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				theBoard[i][j] = SPACE_CHAR;
		markCount = 0;
	}

	/**
	 * Determines if the current state of the board results in a winner.
	 *
	 * Contains logic to check for a sequence of 3 (same) marks in any orientation.
	 *
	 * @param mark the mark of the player to check (X mark or O mark)
	 * @return 1 if board results in a winner; else, 0 if no winner.
	 */
	int checkWinner(char mark) {
		int row, col;
		int result = 0;

		// Checks for wins by 3 marks in a single row
		for (row = 0; result == 0 && row < 3; row++) {
			int row_result = 1;
			for (col = 0; row_result == 1 && col < 3; col++)
				if (theBoard[row][col] != mark)
					row_result = 0;
			if (row_result != 0)
				result = 1;
		}

		// Checks for wins by 3 marks in a single column
		for (col = 0; result == 0 && col < 3; col++) {
			int col_result = 1;
			for (row = 0; col_result != 0 && row < 3; row++)
				if (theBoard[row][col] != mark)
					col_result = 0;
			if (col_result != 0)
				result = 1;
		}

		// Checks for wins by 3 marks along the top-left to bottom-right diagonal
		if (result == 0) {
			int diag1Result = 1;
			for (row = 0; diag1Result != 0 && row < 3; row++)
				if (theBoard[row][row] != mark)
					diag1Result = 0;
			if (diag1Result != 0)
				result = 1;
		}
		// Checks for wins by 3 marks along the bottom-left to top-right diagonal
		if (result == 0) {
			int diag2Result = 1;
			for (row = 0; diag2Result != 0 && row < 3; row++)
				if (theBoard[row][3 - 1 - row] != mark)
					diag2Result = 0;
			if (diag2Result != 0)
				result = 1;
		}
		return result;
	}

	/**
	 * Prints the board's column headers to std out in a evenly-spaced format.
	 */
	void displayColumnHeaders(PrintWriter socketOut) {
		socketOut.print("          ");
		for (int j = 0; j < 3; j++)
			socketOut.print("|col " + j);
		socketOut.println();
	}

	/**
	 * Prints hyphens for formatting purposes. Used for printing the board to std out.
	 */
	void addHyphens(PrintWriter socketOut) {
		socketOut.print("          ");
		for (int j = 0; j < 3; j++)
			socketOut.print("+-----");
		socketOut.println("+");
	}

	/**
	 * Prints spaces for formatting purposes. Used for printing the board to std out.
	 */
	void addSpaces(PrintWriter socketOut) {
		socketOut.print("          ");
		for (int j = 0; j < 3; j++)
			socketOut.print("|     ");
		socketOut.println("|");
	}
}

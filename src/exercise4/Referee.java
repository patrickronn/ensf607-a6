package exercise4;

import java.io.IOException;

/**
 * A class to represent a referee that can initiate a game to begin.
 *
 * @author Patrick Ronn Linang
 * @since September 27, 2020
 */
public class Referee {
    /**
     * Represents the player with the 'X' mark.
     */
    private Player xPlayer;

    /**
     * Represents the player with the 'O' mark.
     */
    private Player oPlayer;

    /**
     * Represents the board that requires refereeing.
     */
    private Board board;

    /**
     * Constructs a referee that's neither assigned to players nor a board.
     */
    public Referee() {
        setxPlayer(null);
        setoPlayer(null);
        setBoard(null);
    }

    /**
     * Initiates a game.
     *
     * Sets two players against each other, displays initial board state,
     * and makes 'X' player go first.
     *
     * @throws IOException error if there's issues when attempting to read a player's move
     */
    public void runTheGame() throws IOException {
        if (xPlayer == null || oPlayer == null)
            throw new IllegalStateException("Cannot run a game without assigning players to the referee.");

        // Make players opponents of each other
        xPlayer.setOpponent(oPlayer);
        oPlayer.setOpponent(xPlayer);

        // 'X' player makes the first move
        xPlayer.play();
    }

    /**
     * Setter method to specify the board to referee.
     * @param board reference to the Board object
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter method to assign oPlayer.
     * @param oPlayer reference to Player object with the 'O' mark
     */
    public void setoPlayer(Player oPlayer) {
        this.oPlayer = oPlayer;
    }

    /**
     * Setter method to assign xPlayer.
     * @param xPlayer reference to Player object with the 'X' mark
     */
    public void setxPlayer(Player xPlayer) {
        this.xPlayer = xPlayer;
    }
}

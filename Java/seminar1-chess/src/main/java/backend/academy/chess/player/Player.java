package backend.academy.chess.player;

import backend.academy.chess.game.Game;

/**
 * The Player represents a User in one specific game. This way the user can act
 * as a Player in many games with different attribute values in each of them.
 */
public class Player extends User {
    // attributes
    private boolean gaveUp = false;
    private boolean winner = false;         // winner saved in Players, because some games may have multiple winners

    private Game game;

    /**********************************
     * Constructors
     **********************************/

    public Player(User user, Game game) {
        super(user);
        this.game = game;
    }

    public void setWinner() {
        winner = true;
    }

    public void surrender() {
        gaveUp = true;
    }

    public void finishGame() {
        //todo:
        game.isFinished();
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isGaveUp() {
        return gaveUp;
    }
}

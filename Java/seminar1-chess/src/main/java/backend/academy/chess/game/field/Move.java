package backend.academy.chess.game.field;

import backend.academy.chess.player.Player;

/**
 * Represents one move of a player in a certain stage of the game.
 * <p>
 * May be specialized further to represent game-specific move information.
 */
public class Move {
    // attributes
    protected String move;
    protected String board;

    // associations
    protected Player player;

    /************************************
     * constructors
     ************************************/

    public Move(String move, String boardBefore, Player player) {
        this.move = move;
        this.board = boardBefore;
        this.player = player;
    }

    /************************************
     * getters/setters
     ************************************/

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

package backend.academy.chess.game;

import backend.academy.chess.player.Player;
import backend.academy.chess.player.User;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract game class, represents aspects common for all game types (also for >2 players);
 * everything necessary for game, player and statistics management.
 */
public abstract class Game {
    // to create unique gameIDs
    protected static int lastID = 0;
    /**********************************
     * Member
     **********************************/

    // attributes (game status)
    protected final int ID;
    // associations
    protected final List<Player> players = new LinkedList<>();
    protected boolean started = false; // game is started
    protected boolean finished = false; // game is over
    protected boolean surrendered = false; // someone gave up; must be finished
    protected Player nextPlayer = null;

    /**********************************
     * Constructors
     **********************************/
    public Game() {
        ID = createID();
    }

    protected static synchronized int createID() {
        return lastID++;
    }


    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<Player> getPlayers() {
        return players;
    }


    public boolean isUsersTurn(User u) {
        if (started && !finished) {
            return u.getId() == nextPlayer.getId();
        }
        return false;
    }

    public boolean isPlayersTurn(Player p) {
        if (started) {
            return p == nextPlayer;
        }
        return false;
    }

    protected boolean finish() {
        // public for tests
        if (started && !finished) {
            finished = true;
            players.forEach(Player::finishGame);
            return true;
        }
        return false;
    }

    /**
     * Returns a String representation of the game state (Zustand), e.g. a String
     * describing the figures of the game board and its figures/positions.
     *
     * @return board representation
     */
    public abstract String getBoard();

    /**
     * Sets any given state (String representation) to the concrete game (internal
     * representation).
     *
     * @param boardFEN string representation of board state (format depends on game)
     */
    public abstract void setBoard(String boardFEN);

    /**
     * Returns name of next player for bot command line.
     *
     * @return Info String (Format left to concrete implementation)
     */
    public abstract String nextPlayerString();

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    /**
     * Adds a player to the game if possible.
     *
     * @param player player object
     * @return true iff successfully added
     */
    public abstract boolean addPlayer(Player player);

    /**
     * This method checks if the supplied move is possible to perform in the current
     * game state/status and, if so, does it. The following has to be checked/might
     * be changed: - it has to be checked if the move can be performed ---- it is a
     * valid move ---- it is done by the right player ---- there is no other move
     * that the player is forced to perform - if the move can be performed, the
     * following has to be done: ---- the board state has to be updated (e.g.
     * figures moved/deleted) ---- the board status has to be updated (check if game
     * is finished) ---- the next player has to be set (if move is over, it's next
     * player's turn) ---- history is updated
     *
     * @param move   String representation of move
     * @param player The player that tries the move
     * @return true if the move was performed
     */
    public abstract boolean tryMove(String move, Player player);


    /**
     * The supplied player gives up. This info has to be saved. The concrete game
     * instance has to finish the game when necessary (for example: one player gives
     * up and loses. The others win)
     *
     * @param player The player who did this.
     * @return true if successful
     */
    public abstract boolean giveUp(Player player);

    public abstract Player getPlayer(User u);
}

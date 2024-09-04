package backend.academy.chess.controll;

import backend.academy.chess.game.CrazyhouseGame;
import backend.academy.chess.game.Game;
import backend.academy.chess.player.HaskellBot;
import backend.academy.chess.player.Player;
import backend.academy.chess.player.User;

/**
 * This class holds game-related use case and additional functionality required
 * by these.
 */
public class GameController {

    public static final String CRAZYHOUSE = "crazyhouse";

    public Game startGame(final User u1, final User u2, final String type) throws Exception {
        Game newGame = createGame(type);

        Player player1 = new Player(u1, newGame);
        Player player2 = new Player(u2, newGame);

        newGame.addPlayer(player1);
        newGame.addPlayer(player2);

        return newGame;
    }

    public Game startGameWithBot(final User u, final String type) throws Exception {
        Game newGame = createGame(type);
        Player player = new Player(u, newGame);
        HaskellBot haskellBot = new HaskellBot(newGame);

        newGame.addPlayer(player);
        newGame.addPlayer(haskellBot);

        return newGame;
    }

    public boolean tryMove(User u, Game g, String move) {
        if (g != null) {

            // game running
            if (!g.isStarted() || g.isFinished()) {
                return false;
            } else {
                return g.tryMove(move, g.getPlayer(u));
            }
        }

        return false;
    }

    private static Game createGame(String gameType) throws Exception {
        try {
            switch (gameType) {
                case CRAZYHOUSE:
                    return new CrazyhouseGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Illegal game type encountered");
    }
}

package backend.academy.chess;

import backend.academy.chess.controll.GameController;
import backend.academy.chess.game.CrazyhouseGame;
import backend.academy.chess.player.Player;
import backend.academy.chess.player.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CrazyhouseGameTest {

    final User user1 = new User(1, "alice");
    final User user2 = new User(-1, "haskellbot");
    final User user3 = new User(2, "bob");

    Player whitePlayer = null;
    Player blackPlayer = null;
    CrazyhouseGame game = null;
    GameController controller;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new GameController();

        game = (CrazyhouseGame) controller.startGameWithBot(user1, "crazyhouse");

        whitePlayer = game.getPlayer(user1);
        blackPlayer = game.getPlayer(user2);
    }

    @Test
    public void testGameStarted() {
        assertFalse(game.addPlayer(new Player(user3, game))); // no third player
        assertTrue(game.isWhiteNext());
    }

    @Test
    public void testSetNextPlayer() {
        game.setNextPlayer(blackPlayer);

        assertFalse(game.isWhiteNext());
    }

    @Test
    public void testCallDrawBoth() {
        game.giveUp(whitePlayer);
        assertTrue(whitePlayer.isGaveUp());
        assertFalse(blackPlayer.isGaveUp());

        game.giveUp(blackPlayer);
        assertFalse(blackPlayer.isGaveUp());
    }

    @Test
    public void testCallDrawBlack() {
        game.giveUp(blackPlayer);
        assertFalse(whitePlayer.isGaveUp());
        assertTrue(blackPlayer.isGaveUp());
    }

    @Test
    public void testGiveUpWhite() {
        game.giveUp(whitePlayer);

        // try after finish
        assertFalse(game.giveUp(whitePlayer));
        assertFalse(game.giveUp(blackPlayer));
    }

    @Test
    public void testNextPlayerString() {
        assertEquals("w", game.nextPlayerString());

        game.setNextPlayer(blackPlayer);

        assertEquals("b", game.nextPlayerString());
    }
}

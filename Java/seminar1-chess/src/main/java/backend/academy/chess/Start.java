package backend.academy.chess;

import backend.academy.chess.controll.GameController;
import backend.academy.chess.game.Game;
import backend.academy.chess.player.Player;
import backend.academy.chess.player.User;
import java.util.List;

public class Start {
    public static void main(String[] args) throws Exception {
        User user1 = new User(1, "user1");
        User user2 = new User(2, "user2");

        GameController gameController = new GameController();
        Game game = gameController.startGameWithBot(
            user1,
            GameController.CRAZYHOUSE
        );

        List<Player> players = game.getPlayers();
        System.out.println("players = " + players);

//        gameController.joinGame(
//            user2,
//            GameFactory.GAME_TYPE_CRAZYHOUSE
//        );

        gameController.tryMove(user1, game, "e2-e4");
        gameController.tryMove(user2, game, "e2-e4");

    }
}

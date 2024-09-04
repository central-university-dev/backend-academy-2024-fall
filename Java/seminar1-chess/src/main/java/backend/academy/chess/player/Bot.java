package backend.academy.chess.player;

import backend.academy.chess.game.Game;
import java.io.IOException;

public abstract class Bot extends Player implements Runnable {
    protected final Game game; // the game this bot plays

    //какой пользователь?
    public Bot(User user, Game game) {
        super(user, game);
        this.game = game;
    }

    protected abstract void executeMove() throws IOException, InterruptedException;

}

package backend.academy.chess.player;

import backend.academy.chess.game.Game;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Bot implementation that launches Haskell program to retrieve single next
 * move.
 */
public class HaskellBot extends Bot implements Runnable {
    protected final String bot; // bot executable
    protected String path; // path of bot executable

    public static final String CRAZYHOUSE_BOT_PATH = "D:\\tmp\\crazyhouse\\";
    public static final String CRAZYHOUSE_BOT_COMMAND = "Bot";

    //какой пользователь?
    public HaskellBot(Game game) {
        super(new User(-1, "haskellbot"), game);
        this.path = CRAZYHOUSE_BOT_PATH;
        this.bot = CRAZYHOUSE_BOT_COMMAND;

        // start a bot poll thread
        new Thread(this).start();
    }

    @SuppressWarnings("all")
    @Override
    public void run() {
        // ToDo: switch to observer pattern

        // run until game is finished
        while (!game.isFinished()) {
            try {
                // check every second for changes
                Thread.sleep(1000);

                // do move when it's my turn
                if (game.isUsersTurn(this)) {
                    executeMove();
                }

            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void executeMove() throws IOException, InterruptedException {

        // Execute command
        final String command = path + bot + " " + game.getBoard() + " " + game.nextPlayerString();
        System.out.println("bot command:" + command);

        final Process child = Runtime.getRuntime()
            .exec(command, null, new File(path));

        // get command line response (wait for bot to finish)
        final BufferedReader bri = new BufferedReader(new InputStreamReader(child.getInputStream()));

        child.waitFor();

        // get result into single string
        StringBuilder result = new StringBuilder();
        while (bri.ready()) {
            result.append(bri.readLine());
        }

        System.out.println("bot answer: " + result + ".");
        // give up when bot didn't find a move (but should have)
        if (result.toString().equals("")) {
            game.giveUp(game.getPlayer(this));
        } else {
            if (!game.tryMove(result.toString(), game.getPlayer(this))) {
                // give up when move was illegal
                game.giveUp(game.getPlayer(this));
            }
        }
    }

}

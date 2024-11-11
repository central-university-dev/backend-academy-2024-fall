package backend.academy.seminar10.refactor_tasks.game;

import java.util.Random;
import java.util.Set;

public class GameRunner {
    public static void main(String[] args) {
        Game game = new Game(Set.of("Chet", "Pat", "Sue"), 7, new OneTimeWordBank());

        Random rand = new Random();
        boolean winner;
        do {
            boolean hasPenalty = game.roll(rand.nextInt(5) + 1);
            winner = !hasPenalty && game.answer(rand.nextInt(9));
        } while (!winner);
    }
}

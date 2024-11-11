package backend.academy.seminar10.refactor_tasks.game;

import java.util.List;
import java.util.Set;

public class Game {
    private final int dummyAnswer;
    private final List<PlayerState> players;
    private final WordBank wordBank;

    // TODO: игра у нас stateful, но как оказалось нужно знать не только состояние игрока/-ов,
    //  но и состояние хода: бросок костей или ответ на вопрос
    private PlayerState player;

    public Game(Set<String> playerNames, int dummyAnswer, WordBank wordBank) {
        if (playerNames.size() < 2) {
            throw new IllegalArgumentException("Game must have at least 2 players");
        }
        this.wordBank = wordBank;
        this.dummyAnswer = dummyAnswer;
        this.players = playerNames.stream().map(PlayerState::new).toList();
        this.player = this.players.get(0);
    }

    public boolean roll(int dice) {
        System.out.println(player.name + " is the current player");
        System.out.println("They have rolled a " + dice);

        attemptToFree(dice);

        if (player.state == PlayerState.State.PENALTY) {
            System.out.println(player.name + " is not getting out of the penalty box");

            nextPlayer();
            return true;
        }

        player.place = (player.place + dice) % 12;
        System.out.println(player.name + "'s new location is " + player.place);
        return false;
    }

    private void attemptToFree(int dice) {
        if (player.state != PlayerState.State.PENALTY) {
            return;
        }

        if (dice % 2 != 0) {
            player.state = PlayerState.State.FREE;
            // TODO: эта информация "зашита" в логику, а должна быть наверху
            System.out.println(player.name + " is getting out of the penalty box");
        }
    }

    public boolean answer(int userAnswer) {
        if (player.state != PlayerState.State.FREE) {
            throw new IllegalStateException("Cannot answer from penalty box");
        }

        // TODO: вопрос задаётся в методе "answer", как это можно было бы изменить?
        //  видится так, что мы можем сделать конечный автомат текущей игровой сессии,
        //  чтобы гарантировать правильный порядок использования методов
        askQuestion();

        boolean winConditions;
        if (userAnswer == dummyAnswer) {
            // TODO: "обработка" (информирование игроков) результатов работы игровой сессии должны происходить "наверху"
            //  для этого опять возвращаемся к состоянию (возвращаемому типу)
            System.out.println("Question was incorrectly answered");
            System.out.println(player.name + " was sent to the penalty box");
            player.state = PlayerState.State.PENALTY;
            winConditions = false;
        } else {
            System.out.println("Answer was correct!!!!");
            player.purse++;
            System.out.println(player.name + " now has " + player.purse + " Gold Coins.");

            winConditions = player.purse == 6;
        }

        nextPlayer();
        return winConditions;
    }

    private void askQuestion() {
        WordBank.Theme category = questionCategoryForPlayer(player);
        System.out.println("The category is " + category);
        String question = wordBank.question(category);
        System.out.println(question);
    }

    private void nextPlayer() {
        if (players.indexOf(player) != (players.size() - 1)) {
            player = players.get(players.indexOf(player) + 1);
        } else {
            player = players.get(0);
        }
    }

    private WordBank.Theme questionCategoryForPlayer(PlayerState player) {
        return switch (player.place) {
            case 0, 4, 8 -> WordBank.Theme.POP;
            case 1, 5, 9 -> WordBank.Theme.SCIENCE;
            case 2, 6, 10 -> WordBank.Theme.SPORT;
            default -> WordBank.Theme.ROCK;
        };
    }

    static class PlayerState {
        final String name;
        int purse;
        int place;
        State state = State.FREE;

        PlayerState(String name) {
            this.name = name;
        }

        enum State {PENALTY, FREE}
    }

}

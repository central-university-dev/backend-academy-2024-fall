package backend.academy.seminar10.refactor_tasks.game;

import java.util.LinkedList;

public class OneTimeWordBank implements WordBank {
    private final LinkedList<String> popQuestions = new LinkedList<>();
    private final LinkedList<String> scienceQuestions = new LinkedList<>();
    private final LinkedList<String> sportsQuestions = new LinkedList<>();
    private final LinkedList<String> rockQuestions = new LinkedList<>();

    public OneTimeWordBank() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast("Rock Question " + i);
        }
    }

    @Override
    public String question(Theme theme) {
        return switch (theme) {
            case POP -> popQuestions.removeFirst();
            case SCIENCE -> scienceQuestions.removeFirst();
            case SPORT -> sportsQuestions.removeFirst();
            case ROCK -> rockQuestions.removeFirst();
        };
    }
}

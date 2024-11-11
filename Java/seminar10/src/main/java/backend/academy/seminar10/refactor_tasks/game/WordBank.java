package backend.academy.seminar10.refactor_tasks.game;

public interface WordBank {
    String question(Theme theme);

    enum Theme {ROCK, POP, SCIENCE, SPORT}
}

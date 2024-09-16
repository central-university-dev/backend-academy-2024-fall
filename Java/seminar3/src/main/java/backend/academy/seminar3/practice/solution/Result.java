package backend.academy.seminar3.practice.solution;

import java.util.function.Consumer;

public sealed interface Result<T> {

    default void ifSuccess(Consumer<? super T> actionOnSuccess) {
        if (this instanceof Success(Object result)) {
            actionOnSuccess.accept((T) result);
        }
    }

    default void ifFailure(Consumer<String> actionOnFailure) {
        if (this instanceof Failure(String message)) {
            actionOnFailure.accept(message);
        }
    }

    default boolean isSuccess() {
        return switch(this) {
            case Success<?> success -> true;
            case Failure<?> failure -> false;
        };
    }

    default boolean isFailure() {
        return !isSuccess();
    }

    record Success<T>(T result) implements Result<T> {
        public void printSuccess() {
            System.out.println(STR."[SUCCESS]: \{result}");
        }
    }

    record Failure<T>(String message) implements Result<T> {
        public void printFailure() {
            System.out.println(STR."[FAILURE]: \{message}");
        }
    }
}

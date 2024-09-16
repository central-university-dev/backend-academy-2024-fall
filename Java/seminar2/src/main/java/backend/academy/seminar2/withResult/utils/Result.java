package backend.academy.seminar2.withResult.utils;

import java.util.Optional;

public class Result<T, E> {
    private final T value;
    private final E error;

    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public static <T, E> Result<T, E> success(T value) {
        return new Result<>(value, null);
    }

    public static <T, E> Result<T, E> failure(E error) {
        return new Result<>(null, error);
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<E> getError() {
        return Optional.ofNullable(error);
    }
}

package backend.academy.seminar2.withResult.utils;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MyOptional<T> {
    private final T value;

    private MyOptional(T value) {
        this.value = value;
    }

    public static <T> MyOptional<T> of(T value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        return new MyOptional<>(value);
    }

    public static <T> MyOptional<T> ofNullable(T value) {
        return new MyOptional<>(value);
    }

    public static <T> MyOptional<T> empty() {
        return new MyOptional<>(null);
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public void ifPresent(Consumer<? super T> action) {
        if (value != null) {
            action.accept(value);
        }
    }

    public <U> MyOptional<U> map(Function<? super T, ? extends U> mapper) {
        if (!isPresent()) {
            return empty();
        } else {
            return MyOptional.ofNullable(mapper.apply(value));
        }
    }

    public <U> MyOptional<U> flatMap(Function<? super T, MyOptional<U>> mapper) {
        if (!isPresent()) {
            return empty();
        } else {
            return mapper.apply(value);
        }
    }

    public MyOptional<T> filter(Function<? super T, Boolean> predicate) {
        if (!isPresent() || predicate.apply(value)) {
            return this;
        } else {
            return empty();
        }
    }

    @Override
    public String toString() {
        return value != null ? String.format("MyOptional[%s]", value) : "MyOptional.empty";
    }
}


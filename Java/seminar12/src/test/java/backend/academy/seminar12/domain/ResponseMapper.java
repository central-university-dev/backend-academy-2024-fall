package backend.academy.seminar12.domain;

import java.util.function.Function;
import lombok.SneakyThrows;


@FunctionalInterface
public interface ResponseMapper<I, O> extends Function<I, O> {

    O map(I response) throws Exception;

    @Override
    @SneakyThrows
    default O apply(I i) {
        return map(i);
    }

}

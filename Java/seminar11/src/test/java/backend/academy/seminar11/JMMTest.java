package backend.academy.seminar11;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
@Disabled // отключен для прохождения пайплайна
public class JMMTest extends AbstractThreadTest {

    private static final int MAX_ATTEMPTS = 1_000;

    @Test
    @SneakyThrows
    @DisplayName("Пример низкоуровневых оптимизаций")
    void test() {
        @Data
        class Tuple {

            int x;
            int y;

        }

        var times = MAX_ATTEMPTS;

        var results = new Tuple[times];
        for (int i = 0; i < times; i++) {
            results[i] = new Tuple();
        }

        while (--times >= 0) {
            var latch = new CountDownLatch(1);
            var holder = new DataHolder();

            final var index = times;
            Thread.ofPlatform()
                    .start(() -> {
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        results[index].setX(holder.getX());
                    });
            Thread.ofPlatform()
                    .start(() -> {
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        results[index].setY(holder.getY());
                    });

            latch.countDown();
            Thread.sleep(100);
        }

        // Работает на MacOS, graalvm-jdk-21
        var actualResults = Arrays.stream(results)
                .map(it -> "X:%s; Y:%s;".formatted(it.getX(), it.getY()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(it -> "\n\t - Значение %s - %s раз".formatted(it.getKey(), it.getValue()))
                .collect(Collectors.joining());
        log.info("Тест выполнен {} раз, полученные в итоге результаты: {}", MAX_ATTEMPTS, actualResults);
    }

    private static class DataHolder {

        private int x; // 0
        private int y; // 0

        public int getY() {
            x = 1;
            return y;
        }

        public int getX() {
            y = 1;
            return x;
        }

    }

}

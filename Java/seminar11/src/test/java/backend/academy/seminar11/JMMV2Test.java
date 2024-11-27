package backend.academy.seminar11;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
@Disabled // отключен для прохождения пайплайна
public class JMMV2Test extends AbstractThreadTest {

    private static final int MAX_ATTEMPTS = 100_000;

    @Test
    @SneakyThrows
    @DisplayName("Пример низкоуровневых оптимизаций")
    void test0() {
        doTest(MAX_ATTEMPTS, DataHolderV1::new);
    }

    @Test
    @SneakyThrows
    @DisplayName("Использование volatile")
    void test1() {
        doTest(MAX_ATTEMPTS, DataHolderV2::new);
    }

    @SneakyThrows
    private static void doTest(int times, Supplier<DataHolder> holderSupplier) {
        var results = new Result[times];
        for (int i = 0; i < times; i++) {
            results[i] = new Result();
        }

        while (--times >= 0) {
            var latch = new CountDownLatch(1);
            var holder = holderSupplier.get();

            final var index = times;
            var t1 = Thread.ofPlatform()
                .start(() -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    results[index].setX(holder.getX());
                });
            var t2 = Thread.ofPlatform()
                .start(() -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    results[index].setY(holder.getY());
                });

            latch.countDown();
            t1.join();
            t2.join();
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

    @Data
    private static class Result {

        int x = -1;
        int y = -1;

    }

    private interface DataHolder {

        int getX();

        int getY();

    }

    private static class DataHolderV1 implements DataHolder {

        private int x; // 0
        private int y; // 0

        @Override
        public int getY() {
            x = 1;
            return y;
        }

        @Override
        public int getX() {
            y = 1;
            return x;
        }

    }

    private static class DataHolderV2 implements DataHolder {

        private volatile int x; // 0
        private volatile int y; // 0

        @Override
        public int getY() {
            x = 1;
            return y;
        }

        @Override
        public int getX() {
            y = 1;
            return x;
        }

    }

}

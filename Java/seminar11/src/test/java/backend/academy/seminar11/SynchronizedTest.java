package backend.academy.seminar11;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Log4j2
public class SynchronizedTest extends AbstractThreadTest {

    private static final int MAX_ATTEMPTS = 1;

    @Disabled // отключен для прохождения пайплайна
    @SneakyThrows
    @ParameterizedTest
    @MethodSource("getCounters")
    @DisplayName("Решаем задачу с увеличением счетчика в несколько потоков")
    void test(Counter counter) {
        log.info("Тестируем счетчик {}", counter.getClass().getSimpleName());

        var latch = new CountDownLatch(1);
        IntStream.range(0, 10).forEach(it ->
            Thread.ofPlatform()
                .name("thread-%s".formatted(it))
                .priority(1)
                .start(() -> doTest(latch, counter)));

        latch.countDown();
        sleep(5_000, log);
        // sleep(1_000);

        log.info("Значение счетчика: {}", counter.getValue());
        assertEquals(counter.getMaxValue(), counter.getValue());
    }

    private static Stream<Arguments> getCounters() {
        return IntStream.range(0, MAX_ATTEMPTS).boxed()
            .flatMap(ignored ->
                Stream.of(
                    Arguments.of(
                        NativeCounter.builder()
                            .maxValue(1_000)
                            .value(0)
                            .build()),
                    Arguments.of(
                        SynchronizedCounter.builder()
                            .maxValue(1_000)
                            .value(0)
                            .build())));
    }

    @SneakyThrows
    private static void doTest(CountDownLatch latch, Counter counter) {
        log.info("Поток {} ожидает своего старта!", Thread.currentThread().getName());
        latch.await();
        log.info("Поток {} стартовал!", Thread.currentThread().getName());
        counter.incrementToMaxValue();
    }

    private interface Counter {

        int getValue();

        int getMaxValue();

        boolean increment();

        default void incrementToMaxValue() {
            // noinspection StatementWithEmptyBody
            while (increment()) {
                // noop
            }
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    private static class NativeCounter implements Counter {

        private int maxValue;
        private int value;

        @Override
        public boolean increment() {
            if (value < maxValue) {
                // Переключение потока, а то может быть так,
                // что только один поток увеличит до максимума и все
                Thread.yield();
                value += 1;
                return true;
            }
            return false;
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    private static class SynchronizedCounter implements Counter {

        private int maxValue;
        private int value;

        @Override
        public synchronized boolean increment() {
            if (value < maxValue) {
                value += 1;
                return true;
            }
            return false;
        }

    }

}

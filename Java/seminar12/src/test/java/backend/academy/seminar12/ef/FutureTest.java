package backend.academy.seminar12.ef;

import backend.academy.seminar12.AbstractConcurrentTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static java.util.concurrent.Future.State.CANCELLED;
import static java.util.concurrent.Future.State.RUNNING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Log4j2
public class FutureTest extends AbstractConcurrentTest {

    @Test
    @DisplayName("Базовая работа с Future")
    void test0() {
        try (var service = Executors.newFixedThreadPool(1)) {
            var future = service.submit(() -> {
                var iteration = 0;
                while (iteration++ < 60 && !currentThread().isInterrupted()) {
                    log.info("Ping!");
                    Thread.sleep(1_000);
                }
                log.info("Pong!");
                return null;
            });

            var state = future.state();
            assertEquals(state, RUNNING);

            future.cancel(true);
            state = future.state();
            assertEquals(state, CANCELLED);

            assertTrue(future.isDone());
            assertTrue(future.isCancelled());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Новшества Future в 19 Java")
    void test1() {
        try (var service = Executors.newFixedThreadPool(1)) {
            var future = service.submit(() -> {
                var iteration = 0;
                while (iteration++ < 5 && !currentThread().isInterrupted()) {
                    log.info("Ping!");
                    Thread.sleep(1_000);
                }
                log.info("Pong!");
                return 30;
            });

            assertThrows(IllegalStateException.class, future::resultNow);
            assertThrows(IllegalStateException.class, future::exceptionNow);

            var result = future.get(6_000, TimeUnit.MILLISECONDS);
            assertEquals(30, result);

            assertEquals(30, future.resultNow());
            assertThrows(IllegalStateException.class, future::exceptionNow);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Работаем с множеством Future'ов - как дождаться выполнения всех?")
    void test2() {
        final var random = new Random();
        Callable<Integer> longComputation = () -> {
            var randInt = random.nextInt(100, 1000);
            Thread.sleep(randInt);
            return randInt;
        };

        try (var service = Executors.newFixedThreadPool(3)) {
            var futures = IntStream.range(0, 10)
                .mapToObj(it -> service.submit(longComputation))
                .toList();

            // 1. Basic
            var results = futures.stream()
                .map(it -> {
                    try {
                        return it.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
            assertEquals(10, results.size());
            log.info("Basic - DONE!");

            // 2. via invokeAll
            results = service.invokeAll(List.of(longComputation, longComputation, longComputation))
                .stream()
                .map(Future::resultNow)
                .toList();
            assertEquals(3, results.size());

            // 3. via CompletionService
            var completionService = new ExecutorCompletionService<Integer>(service);
            IntStream.range(0, 10).forEach(it -> completionService.submit(longComputation));

            results = new ArrayList<>();
            while (results.size() != 10) {
                Future<Integer> future;
                while ((future = completionService.poll()) == null) { // also see take
                    log.info("No tasks are ready, waiting...");
                    Thread.sleep(1_000);
                }
                results.add(future.resultNow());
            }
            assertEquals(10, results.size());
            log.info("Completion - DONE!");
        }
    }

}

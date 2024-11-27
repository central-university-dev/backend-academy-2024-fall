package backend.academy.seminar12.ef;

import backend.academy.seminar12.AbstractConcurrentTest;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Log4j2
public class ExecutorTest extends AbstractConcurrentTest {

    @Test
    @SneakyThrows
    @DisplayName("Пример создания и использования ExecutorService с фиксированным кол-вом потоков")
    void test0() {
        try (var service = Executors.newFixedThreadPool(3)) {
            var times = new AtomicInteger();
            while (times.get() < 10) {
                service.submit(() -> {
                    var time = times.incrementAndGet();
                    log.info("Hello, world x{} from thread {}", time, currentThreadName());
                });
                Thread.sleep(10);
            }
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Освобождение ресурсов из под ExecutorService")
    void test1() {
        var useJava19 = true;
        var service = Executors.newFixedThreadPool(3);
        try {
            // doing something
        } finally {
            if (useJava19) {
                service.close(); // Since Java 19
            } else {
                // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
                service.shutdown(); // Disable new tasks from being submitted
                try {
                    // Wait a while for existing tasks to terminate
                    if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                        service.shutdownNow(); // Cancel currently executing tasks
                        // Wait a while for tasks to respond to being cancelled
                        if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                            log.error("Pool did not terminate");
                        }
                    }
                } catch (InterruptedException ie) {
                    // (Re-)Cancel if current thread also interrupted
                    service.shutdownNow();
                    // Preserve interrupt status
                    currentThread().interrupt();
                }
            }
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Освобождение ресурсов из под ExecutorService - пример")
    void test2() {
        // noinspection resource
        var service = Executors.newFixedThreadPool(1);

        service.submit(() -> {
            var iteration = 0L;
            while (!currentThread().isInterrupted()) {
                log.info("Infinite ping loop. {} iteration.", iteration++);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException exception) {
                    log.info("Thread has been interrupted, meaning that task is being cancelled.");
                    currentThread().interrupt();
                }
            }
        });

        service.submit(() -> {
            for (int iteration = 0; iteration < 1_000; iteration++) {
                log.info("Ping x{}", iteration);
            }
        });

        service.shutdown(); // Начиная отсюда мы не сможем сабмитить задачи

        assertThrows(
            RejectedExecutionException.class,
            () -> service.submit(() -> System.out.println("Hello, world!")));

        Thread.sleep(1_000); // Но задачи все еще крутятся

        var result = service.shutdownNow(); // Потокам послан сигнал о прерывании
        assertEquals(1, result.size());

        assertTrue(service.awaitTermination(30, TimeUnit.SECONDS));
    }

    @Test
    @SneakyThrows
    @DisplayName("Разница между submit & invoke")
    void test3() {
        try (var service = Executors.newFixedThreadPool(5)) {
            // submit
            var future = service.submit(() -> {
                var iteration = 0;
                while (iteration++ < 10) {
                    log.info("Ping!");
                    Thread.sleep(100);
                }
                log.info("Pong!");
                return null;
            });

            assertFalse(future.isDone());
            Thread.sleep(1_500);
            assertTrue(future.isDone());

            // invoke
            var tasks = List.<Callable<Integer>>of(
                () -> sleepAndGet(2_000, () -> 256),
                () -> sleepAndGet(1_000, () -> 101),
                () -> sleepAndGet(500, () -> 30));

            log.info("Calling invokeAny...");
            var value = service.invokeAny(tasks);
            assertEquals(30, value);

            log.info("Calling invokeAll...");
            var start = System.currentTimeMillis();
            var futures = service.invokeAll(tasks);
            assertTrue(futures.stream().allMatch(Future::isDone));

            var timeSpent = System.currentTimeMillis() - start;
            assertTrue(timeSpent > 1_500);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Планирование задач с помощью ScheduledThreadPool")
    void test4() {
        try (var service = Executors.newScheduledThreadPool(2)) {
            service.scheduleAtFixedRate(() -> log.info("Ping!"), 0, 1, TimeUnit.SECONDS);
            Thread.sleep(5_000);
        }
    }

    @SneakyThrows
    private static <V> V sleepAndGet(int delay, Supplier<V> supplier) {
        Thread.sleep(delay);
        return supplier.get();
    }

}

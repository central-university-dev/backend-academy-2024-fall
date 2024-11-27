package backend.academy.seminar12.collections;

import backend.academy.seminar12.AbstractConcurrentTest;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
public class ConcurrentMapsTest extends AbstractConcurrentTest {

    @Test
    @SneakyThrows
    @DisplayName("Демонстрация работы различных коллекций в многопоточном окружении")
    void test0() {
        for (var map : getMaps()) {
            var latch = new CountDownLatch(1);
            final var key = "test" + map.getClass().getSimpleName();
            IntStream.range(0, 5).forEach(ignored ->
                startThread(() -> {
                    latch.await();
                    var exists = map.putIfAbsent(key, key + ignored) != null;
                    log.info(
                        "Ключ {} присутствует в коллекции? {}.",
                        key, exists ? "Да" : "Нет"
                    );
                }));

            latch.countDown();
            Thread.sleep(1_000);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Демонстрация производительности различных коллекций в многопоточном окружении")
    void test1() {
        var maps = List.of(
            new HashMap<String, String>(),
            Collections.synchronizedMap(new HashMap<String, String>()),
            new ConcurrentHashMap<String, String>()
        );
        for (var map : maps) {
            final var iterations = 1_000;
            var totalTimeSpent = new AtomicLong();
            for (int i = 0; i < iterations; i++) {
                var latch = new CountDownLatch(1);
                var threads = IntStream.range(0, 5).mapToObj(ignored ->
                    startThread(() -> {
                        latch.await();
                        final var key = "test";
                        var start = System.nanoTime();
                        map.putIfAbsent(key, key);
                        totalTimeSpent.addAndGet(System.nanoTime() - start);
                    })).toList();
                latch.countDown();
                for (var thread : threads) {
                    thread.join();
                }
            }
            log.info(
                "Среднее время выполнения операции putIfAbsent для {}: {} нс.",
                map.getClass().getSimpleName(), totalTimeSpent.get() / (double) iterations
            );
        }
    }

    private static List<Map<String, String>> getMaps() {
        var notSyncedMap = new TreeMap<String, String>((left, right) -> {
            Thread.yield(); // нужно, чтобы все не выполнялось на одном потоке
            return Objects.compare(left, right, Comparator.naturalOrder());
        });

        // Note: тут можно посмотреть на устройство синхронизированных мап
        var syncedMap = Collections.synchronizedMap(
            new TreeMap<String, String>((left, right) -> {
                Thread.yield(); // нужно, чтобы все не выполнялось на одном потоке
                return Objects.compare(left, right, Comparator.naturalOrder());
            })
        );

        var concurrentHashMap = new ConcurrentHashMap<String, String>();

        return List.of(notSyncedMap, syncedMap, concurrentHashMap);
    }

}

package backend.academy.seminar12.cf;

import backend.academy.seminar12.AbstractConcurrentTest;
import backend.academy.seminar12.domain.events.EnrichedEvent;
import backend.academy.seminar12.domain.events.EventUtils;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
public class CompletableFuturePipelineTest extends AbstractConcurrentTest {

    private static final Random RANDOM = new Random();

    @Test
    @Disabled
    @SneakyThrows
    @DisplayName("Пример относительно сложного пайплайна")
    void test() {
        try (
            var ioExecutor = Executors.newFixedThreadPool(5);
            var processingExecutor = Executors.newFixedThreadPool(3)) {

            final var futureQueue = new CopyOnWriteArrayList<>(
                IntStream.range(1, 11)
                    .mapToObj(page -> getTopRatedEventsAsync(page, 100, ioExecutor))
                    .toList());

            CompletableFuture.runAsync(() -> log.info("Начинаем обработку результатов!"), processingExecutor)
                .thenCompose(ignored1 -> CompletableFuture.anyOf(futureQueue.toArray(CompletableFuture[]::new))
                    .thenCompose(ignored2 -> handleCompletedFutures(futureQueue, processingExecutor)))
                .join();
        }
    }

    private static CompletableFuture<?> handleCompletedFutures(
        List<CompletableFuture<List<EnrichedEvent>>> futures,
        ExecutorService executor
    ) {
        log.info("Какие-то события получены, собираем результат!");
        var completedFutures = futures.stream()
            .filter(Future::isDone)
            .toList();
        futures.removeAll(completedFutures);

        var delay = RANDOM.nextInt(100, 500);
        log.info(
            "Выполнены {} запросов, обрабатываем их на протяжении {} мс!",
            completedFutures.size(), delay);
        sleep(delay);

        if (futures.isEmpty()) {
            return CompletableFuture.runAsync(() -> log.info("Обработка результатов завершена!"), executor);
        }

        return CompletableFuture.anyOf(futures.toArray(CompletableFuture[]::new))
            .thenCompose(ignored -> handleCompletedFutures(futures, executor));
    }

    private CompletableFuture<List<EnrichedEvent>> getTopRatedEventsAsync(
        int page, int favoritesThreshold,
        ExecutorService executor
    ) {
        return CompletableFuture.supplyAsync(
                () -> {
                    var delay = RANDOM.nextInt(1_000, 2_000);
                    log.info("Получаем события со страницы {} с задержкой {} мс", page, delay);
                    sleep(delay);
                    return EventUtils.getEventsPage(page);
                },
                executor)
            .thenApplyAsync(
                events -> events.stream()
                    .filter(it -> it.favorites() > favoritesThreshold)
                    .toList(),
                executor);
    }

}

package backend.academy.seminar12.collections;

import backend.academy.seminar12.AbstractConcurrentTest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
public class BlockingQueueTest extends AbstractConcurrentTest {

    @Test
    @SneakyThrows
    @DisplayName("Знакомство со структурой данных BlockingQueue на примере LinkedBlockingQueue")
    void test0() {
        final var queue = new LinkedBlockingQueue<String>(2);

        var producer = startBlockingProducer(queue, 1_000);
        var consumer = startBlockingConsumer(queue, 2_000);

        observe(queue, producer, consumer, 10_000, 500);
    }

    @Test
    @SneakyThrows
    @DisplayName("Неблокирующая BlockingQueue на примере LinkedBlockingQueue")
    void test1() {
        final var queue = new LinkedBlockingQueue<String>(2);

        var producer = startNonBlockingProducer(queue, 500);
        var consumer = startNonBlockingConsumer(queue, 2_000);

        observe(queue, producer, consumer, 10_000, 500);
    }

    private static Thread startBlockingProducer(LinkedBlockingQueue<String> queue, int delay) {
        return startThread(() -> {
            var iteration = 0;
            while (!currentThread().isInterrupted()) {
                Thread.sleep(delay); // эмулируем вычислительную нагрузку
                queue.put("value" + iteration++); // блокирует поток до тех пор, пока не освободится место в очереди
            }
        });
    }

    private static Thread startBlockingConsumer(LinkedBlockingQueue<String> queue, int delay) {
        return startThread(() -> {
            while (!currentThread().isInterrupted()) {
                var value = queue.take();
                Thread.sleep(delay); // эмулируем вычислительную нагрузку
                log.info("Консьюмер получил следующее значение от продьюсера: {}", value);
            }
        });
    }

    private static Thread startNonBlockingProducer(LinkedBlockingQueue<String> queue, int delay) {
        return startThread(() -> {
            var iteration = 0;
            while (!currentThread().isInterrupted()) {
                Thread.sleep(delay); // эмулируем вычислительную нагрузку
                var value = "value" + iteration++;
                if (!queue.offer(value, delay, TimeUnit.MILLISECONDS)) {
                    log.warn("Значение {} никто не готов обрабатывать, поэтому мы его игнорируем", value);
                }
            }
        });
    }

    private static Thread startNonBlockingConsumer(LinkedBlockingQueue<String> queue, int delay) {
        return startThread(() -> {
            while (!currentThread().isInterrupted()) {
                var value = queue.poll(delay, TimeUnit.MILLISECONDS);
                if (value == null) {
                    log.warn("Для консьюмера нет значений в очереди, поэтому он уходит в спячку!");
                    Thread.sleep(delay * 2L);
                    continue;
                }

                Thread.sleep(delay); // эмулируем вычислительную нагрузку
                log.info("Консьюмер получил следующее значение от продьюсера: {}", value);
            }
        });
    }

    private static void observe(
        LinkedBlockingQueue<String> queue,
        Thread producer, Thread consumer,
        long totalObservationTime,
        long observationPeriod
    ) throws InterruptedException {
        var iterations = totalObservationTime / observationPeriod;
        for (int i = 0; i < iterations; i++) {
            log.info(
                "!!! В очереди {} элементов, состояние продьюсера: {}, состояние консьюмера: {}",
                queue.size(), producer.getState(), consumer.getState()
            );
            Thread.sleep(observationPeriod);
        }
    }

    private static void finish(Thread producer, Thread consumer) throws InterruptedException {
        producer.interrupt();
        consumer.interrupt();
        producer.join();
        consumer.join();
    }

}

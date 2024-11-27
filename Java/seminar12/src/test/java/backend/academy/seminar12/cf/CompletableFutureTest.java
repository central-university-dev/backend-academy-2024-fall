package backend.academy.seminar12.cf;

import backend.academy.seminar12.AbstractConcurrentTest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Log4j2
public class CompletableFutureTest extends AbstractConcurrentTest {

    private static final int HEAVILY_COMPUTED_ULTIMATE_VALUE_1 = 30;
    private static final int HEAVILY_COMPUTED_ULTIMATE_VALUE_2 = 101;

    @Test
    @SneakyThrows
    @DisplayName("Базовая работа с CompletableFuture - как их создавать и что под капотом")
    void test0() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            // Можно посмотреть на другие способы создания CompletableFuture
            var completableFuture = CompletableFuture.supplyAsync(
                heavilyComputedSupplier(HEAVILY_COMPUTED_ULTIMATE_VALUE_1),
                executor);

            // С CompletableFuture можно работать и по-старинке, и с помощью "нового" API
            Future<Integer> future = completableFuture;
            CompletionStage<Integer> stage = completableFuture;

            // Как CompletableFuture работает под капотом?
            // Можно рассказать и посмотреть на:
            // volatile Object result;       // Either the result or boxed AltResult
            // volatile Completion stack;    // Top of Treiber stack of dependent actions
            // Реализации Completion (их много)
            completableFuture
                .thenApplyAsync(it -> it + 1, executor) // Следующий элемент в стеке стейджей
                .thenAccept(it -> log.info("Итоговое значение: {}", it)) // Следующий элемент в стеке стейджей
                .join();
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Как завершить работу CompletableFuture - complete-методы")
    void test1() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            // Можно посмотреть на другие способы создания CompletableFuture
            var completableFuture = CompletableFuture.supplyAsync(
                heavilyComputedSupplier(HEAVILY_COMPUTED_ULTIMATE_VALUE_1),
                executor);

            // Note: обратите внимание на логи!
            Thread.sleep(100);
            completableFuture.complete(HEAVILY_COMPUTED_ULTIMATE_VALUE_2);
            assertEquals(HEAVILY_COMPUTED_ULTIMATE_VALUE_2, completableFuture.get());
            log.warn(
                "Завершаем работу теста! Значение completableFuture нам уже известно и вычисления более не нужны!");
            Thread.sleep(1000);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Как завершить работу CompletableFuture - cancel-методы")
    void test2() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            // Можно посмотреть на другие способы создания CompletableFuture
            var completableFuture = CompletableFuture.supplyAsync(
                heavilyComputedSupplier(HEAVILY_COMPUTED_ULTIMATE_VALUE_1),
                executor);

            // Note: обратите внимание на логи! Прерывания потока не будет!
            Thread.sleep(100);
            log.warn("Cancelling future!");
            completableFuture.cancel(true);
            assertEquals(Future.State.CANCELLED, completableFuture.state());
            assertTrue(completableFuture.isCancelled());
            log.warn("Future is cancelled!");
            Thread.sleep(1000);
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Как завершить работу CompletableFuture - timeout")
    void test3() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            // Можно посмотреть на другие способы создания CompletableFuture
            var completableFuture = CompletableFuture.supplyAsync(
                    heavilyComputedSupplier(HEAVILY_COMPUTED_ULTIMATE_VALUE_1),
                    executor)
                .orTimeout(100, TimeUnit.MILLISECONDS);

            var ex = assertThrows(CompletionException.class, completableFuture::join);
            assertInstanceOf(TimeoutException.class, ex.getCause());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Пропагация завершения пайплайна сверху вниз")
    void test4() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            // Можно посмотреть на другие способы создания CompletableFuture
            var start = CompletableFuture.supplyAsync(
                heavilyComputedSupplier(HEAVILY_COMPUTED_ULTIMATE_VALUE_1),
                executor);
            var end = start.thenRunAsync(() -> log.info("Finishing the pipeline!"));

            Thread.sleep(100);
            start.completeExceptionally(new UnsupportedOperationException("I don't need the pipeline anymore!"));

            assertTrue(start.isCompletedExceptionally());
            assertTrue(end.isCompletedExceptionally());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Пропагация завершения пайплайна снизу вверх?")
    void test5() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            // Можно посмотреть на другие способы создания CompletableFuture
            var start = CompletableFuture.supplyAsync(
                heavilyComputedSupplier(HEAVILY_COMPUTED_ULTIMATE_VALUE_1),
                executor);
            var end = start.thenRunAsync(() -> log.info("Finishing the pipeline!"));

            Thread.sleep(100);
            end.completeExceptionally(new UnsupportedOperationException("I don't need the pipeline anymore!"));

            assertFalse(start.isCompletedExceptionally());
            assertFalse(start.isDone());
            assertTrue(end.isCompletedExceptionally());

            assertEquals(HEAVILY_COMPUTED_ULTIMATE_VALUE_1, start.join());
        }
    }

    @Test
    @SneakyThrows
    @DisplayName("Реальная асинхронность - где оно все исполняется?")
    void test6() {
        try (var executor = Executors.newFixedThreadPool(2)) {
            CompletableFuture
                .runAsync(() -> log.info("Calling runAsync in thread {}", currentThreadName()), executor)
                .thenRunAsync(() -> log.info("Calling thenRunAsync in thread {}", currentThreadName()))
                .thenRun(() -> log.info("Calling thenRun in thread {}", currentThreadName()))
                .join();
        }
    }

    private static <T> Supplier<T> heavilyComputedSupplier(T value) {
        return heavilyComputedSupplier(() -> value);
    }

    private static <T> Supplier<T> heavilyComputedSupplier(Supplier<T> supplier) {
        return () -> {
            for (int i = 0; i < 10; i++) {
                log.debug(
                    "Br-br-br, calculating some heavy value!");
                sleep(100, log);
            }
            return supplier.get();
        };
    }

}

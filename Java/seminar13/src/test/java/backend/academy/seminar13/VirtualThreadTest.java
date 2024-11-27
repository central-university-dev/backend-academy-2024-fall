package backend.academy.seminar13;

import java.util.concurrent.Executors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Disabled
public class VirtualThreadTest {

    @Test
    @SneakyThrows
    @DisplayName("Пример работы с виртуальными потоками")
    void test0() {
        System.out.print("Hello");
        for (int i = 0; i < 100_000; i++) {
            final var delay = (long) i;
            Thread.ofVirtual().start(() -> {
                try {
                    // todo: look under the hood
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.print(".");
            });
        }
        Thread.sleep(10_000);
        System.out.println("World!");
    }

    @Test
    @DisplayName("Виртуальные потоки не панацея!")
    void test1() {
        // Кол-во потоков в ForkJoinPool'е, на котором будут работать виртуальные потоки
        var availableProcessors = Runtime.getRuntime().availableProcessors();
        var factory = Thread.ofVirtual().name("virtual-", 0).factory();
        try (var executor = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < availableProcessors - 1; i++) {
                executor.submit(() -> {
                    // забиваем пул потоков бесконечными синхронными неблокирующими операциями
                    long counter = Long.MIN_VALUE;
                    while (counter <= Long.MAX_VALUE) {
                        if (counter >= Long.MAX_VALUE / 2) {
                            counter = Long.MIN_VALUE;
                        }
                        counter++;
                    }
                });
            }

            Runnable task = () -> {
                long counter = Long.MIN_VALUE;
                while (counter++ < 0) {
                    if (counter % 1_000_000 == 0) {
                        System.out.printf("Еще один миллион в потоке %s!%n", Thread.currentThread().getName());
                        // todo: расскомментировать для "параллелизации"
                        // Thread.yield();
                    }
                }
            };

            executor.submit(task);
            executor.submit(task);
        }
    }

}

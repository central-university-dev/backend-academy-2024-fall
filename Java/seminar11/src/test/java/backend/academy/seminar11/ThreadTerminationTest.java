package backend.academy.seminar11;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Log4j2
public class ThreadTerminationTest extends AbstractThreadTest {

    @Test
    @SneakyThrows
    @DisplayName("Нормальное завершение работы потока")
    void test0() {
        var tickThread = new Thread(() -> {
            var ticks = 5;
            while (ticks-- > 0) {
                log.info("Tick!");
                sleep(1_000, () -> log.warn("Ticking has been interrupted!"));
            }
            log.info("Tuck!");
        });

        tickThread.start();
        tickThread.join();
        assertEquals(tickThread.getState(), Thread.State.TERMINATED);
    }

    @Test
    @SneakyThrows
    @DisplayName("Завершение работы потока из-за исключения")
    void test1() {
        var tickThread = new Thread(() -> {
            var ticks = 5;
            while (true) {
                log.info("Tick!");
                if (ticks-- <= 0) {
                    throw new IllegalStateException("Too many ticks for today!");
                }
                sleep(1_000, () -> log.warn("Ticking has been interrupted!"));
            }
        });

        tickThread.start();
        tickThread.join();
        assertEquals(tickThread.getState(), Thread.State.TERMINATED);
    }

    @Test
    @SneakyThrows
    @DisplayName("Завершение работы потока из-за прерывания")
    void test2() {
        var tickThread = new Thread(() -> {
            while (true) {
                log.info("Tick!");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    // InterruptedException в любом случае надо обработать,
                    // тут мы его просто прокидываем в виде RuntimeException
                    throw new RuntimeException(e);
                }
            }
        });

        tickThread.start();
        Thread.sleep(3_000);

        tickThread.interrupt();
        tickThread.join();
        assertEquals(tickThread.getState(), Thread.State.TERMINATED);
    }

    @Test
    @SneakyThrows
    @DisplayName("Кастомная обработка ошибок")
    void test3() {
        var tickThread = Thread.ofPlatform()
            .uncaughtExceptionHandler((thread, exception) ->
                log.error(
                    "Thread {} finished with error. Its current state is: {}.",
                    Thread.currentThread().getName(),
                    Thread.currentThread().getState(),
                    exception))
            .start(() -> {
                while (true) {
                    log.info("Tick!");
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        // InterruptedException в любом случае надо обработать,
                        // тут мы его просто прокидываем в виде RuntimeException
                        throw new RuntimeException(e);
                    }
                }
            });

        Thread.sleep(3_000);

        tickThread.interrupt();
        tickThread.join();
        assertEquals(tickThread.getState(), Thread.State.TERMINATED);
    }

}

package backend.academy.seminar11;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Log4j2
public class ThreadInterruptionTest extends AbstractThreadTest {

    @Test
    @SneakyThrows
    @DisplayName("Прерывание работы потока из коробки")
    void test0() {
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
        assertEquals(Thread.State.TERMINATED, tickThread.getState());
    }

    @Test
    @SneakyThrows
    @DisplayName("Прерывание работы непрерываемого потока")
    void test1() {
        var tickThread = new Thread(() -> {
            var counter = 0L;
            while (true) {
                counter++;
                if (counter >= Long.MAX_VALUE / 2) {
                    counter = 0;
                }
            }
        });

        tickThread.start();
        Thread.sleep(3_000);

        tickThread.interrupt();
        tickThread.join(5_000);

        assertEquals(Thread.State.RUNNABLE, tickThread.getState());
    }

    @Test
    @SneakyThrows
    @DisplayName("Graceful-прерывание потока")
    void test2() {
        var tickThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { // 1. Проверяем прерывание
                log.info("Tick!");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    log.warn("Ticking has been interrupted, graceful shutdown is in progress!");
                    Thread.currentThread().interrupt(); // 2. Пропагируем прерывание
                }
            }
        });

        tickThread.start();
        tickThread.join(5_000);
        tickThread.interrupt();

        Thread.sleep(1_000);
        assertEquals(tickThread.getState(), Thread.State.TERMINATED);
    }

}

package backend.academy.seminar11;

import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
public class WaitNotifyTest extends AbstractThreadTest {

    @Test
    @SneakyThrows
    @DisplayName("Пример использования wait & notify")
    void test() {
        var sync = new Object();

        IntStream.range(0, 10).forEach(it ->
            Thread.ofPlatform()
                .name("waiter-%s".formatted(it))
                .start(() -> doTest(sync)));

        Thread.sleep(1000);
        synchronized (sync) {
            log.info("Начинаем работу очереди!");
            sync.notifyAll();
            sleep(1000, log);
            log.info("А вот теперь реально начинаем работу очереди!");
        }

        Thread.sleep(10_000);
    }

    @SneakyThrows
    private static void doTest(Object sync) {
        log.info("Поток {} ожидает своей очереди", Thread.currentThread().getName());
        synchronized (sync) {
            sync.wait();
            log.info("Поток {} дождался своей очереди", Thread.currentThread().getName());

            Thread.sleep(100);

            sync.notifyAll();
            log.info("Поток {} уведомил всех о наличии свободного места в очереди", Thread.currentThread().getName());
        }
        log.info("Поток {} отпустил монитор на синхронизаторе", Thread.currentThread().getName());
    }

}

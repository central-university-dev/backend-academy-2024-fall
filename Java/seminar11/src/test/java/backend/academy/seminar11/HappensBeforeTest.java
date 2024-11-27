package backend.academy.seminar11;

import java.util.concurrent.CountDownLatch;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
@Disabled // отключен для прохождения пайплайна
public class HappensBeforeTest {

    @Test
    @SneakyThrows
    @DisplayName("Демонстрация семантики happens-before на упрощенном примере")
    void test() {
        var latch = new CountDownLatch(1);
        var provider = new ResultProvider();

        var waiter = Thread.ofPlatform()
            .start(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("Waiter запущен!");
                provider.await();
                log.info("Waiter завершил работу!");
            });
        var computer = Thread.ofPlatform()
            .start(() -> {
                try {
                    latch.await();
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("Provider запущен!");
                provider.complete(42);
                log.info("Provider завершил работу!");
            });

        latch.countDown();
        computer.join();
        waiter.join(10_000);
        // log.error("Прошло уже 10 секунд, а Waiter все еще работает!");
    }

    private static class ResultProvider {

        private volatile boolean resultIsReady;
        private int result;

        int await() {
            while (!resultIsReady) {
                // do nothing
            }
            return result;
        }

        void complete(int value) {
            result = value;
            resultIsReady = true;
        }

    }

}

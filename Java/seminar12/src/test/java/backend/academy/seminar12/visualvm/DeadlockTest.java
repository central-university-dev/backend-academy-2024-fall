package backend.academy.seminar12.visualvm;

import backend.academy.seminar12.AbstractConcurrentTest;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@Log4j2
@Disabled
public class DeadlockTest extends AbstractConcurrentTest {

    @Test
    @SneakyThrows
    @DisplayName("Пример дедлока для профилирования")
    void test() {
        Object resource1 = new Object();
        Object resource2 = new Object();

        Thread threadA = new Thread(() -> {
            synchronized (resource1) {
                log.info("ThreadA: Holding lock 1...");
                sleep(1_000, log);

                log.info("ThreadA: Waiting for lock 2...");
                synchronized (resource2) {
                    log.info("ThreadA: Holding lock 1 & 2...");
                }
            }
        });
        Thread threadB = new Thread(() -> {
            synchronized (resource2) {
                log.info("ThreadB: Holding lock 2...");
                sleep(1_000, log);

                log.info("ThreadB: Waiting for lock 1...");
                synchronized (resource1) {
                    log.info("ThreadB: Holding lock 1 & 2...");
                }
            }
        });

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
    }

}

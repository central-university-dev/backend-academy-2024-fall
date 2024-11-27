package backend.academy.seminar11;

import org.apache.logging.log4j.Logger;


abstract class AbstractThreadTest {

    protected static void sleep(long sleepMs, Logger log) {
        sleep(sleepMs, () -> log.warn("Thread {} has been interrupted!", Thread.currentThread().getName()));
    }

    protected static void sleep(long sleepMs, Runnable onInterruption) {
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            onInterruption.run();
            Thread.currentThread().interrupt();
        }
    }

}

package backend.academy.seminar12;

import lombok.SneakyThrows;
import org.apache.logging.log4j.Logger;


public abstract class AbstractConcurrentTest {

    protected static void sleep(long sleepMs) {
        sleep(sleepMs, () -> {
        });
    }

    protected static void sleep(long sleepMs, Logger log) {
        sleep(sleepMs, () -> log.warn("Thread {} has been interrupted!", currentThread().getName()));
    }

    protected static void sleep(long sleepMs, Runnable onInterruption) {
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            onInterruption.run();
            currentThread().interrupt();
        }
    }

    protected static Thread startThread(InterruptibleRunnable runnable) {
        return Thread.ofPlatform().start(runnable);
    }

    protected static Thread currentThread() {
        return Thread.currentThread();
    }

    protected static String currentThreadName() {
        return currentThread().getName();
    }

    protected interface InterruptibleRunnable extends Runnable {

        void runV2() throws Exception;

        @Override
        @SneakyThrows
        default void run() {
            runV2();
        }

    }

}

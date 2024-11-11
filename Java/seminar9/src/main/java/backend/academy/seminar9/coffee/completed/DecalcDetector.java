package backend.academy.seminar9.coffee.completed;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple decalcification detector, relying on counter.
 * If counter reaches specified {@code threshold}, then decalcification is required
 */
public class DecalcDetector {

    private final AtomicInteger counter = new AtomicInteger();

    private final int threshold;

    public DecalcDetector(int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }

        this.threshold = threshold;
    }

    public int getCounter() {
        return counter.get();
    }

    public boolean isDecalcificationRequired() {
        return counter.get() >= threshold;
    }

    public void reset() {
        counter.set(0);
    }

    public void incrementCounter() {
        incrementCounter(1);
    }

    public void incrementCounter(int increment) {
        if (increment < 0) {
            throw new IllegalArgumentException("Increment must be greater than 0");
        }

        counter.addAndGet(increment);
        if (counter.get() < 0) {
            counter.set(threshold);
        }
    }
}

package backend.academy.range;

import backend.academy.dto.PaymentRequest;
import java.util.Iterator;

/**
 *
 * @param start is inclusive
 * @param end is exclusive
 */
public record Range(int start, int end) implements Iterable<Integer> {
    public Range {
        if (start >= end) {
            throw new IllegalArgumentException(
                "Start must be less than end"
            );
        }
    }

    public Range(int end) { this(0, end); }

    public Range getFirstHalf() { return new Range(start, (start + end) / 2); }

    public Range getSecondHalf() { return new Range((start + end) / 2, end); }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            private int currentValue = start;

            @Override
            public boolean hasNext() {
                return currentValue < end;
            }

            @Override
            public Integer next() {
                return currentValue++;
            }
        };
    }
}

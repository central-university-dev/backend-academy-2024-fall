package backend.academy.range;

import java.util.Iterator;
import java.util.Objects;

public class RangeOld implements Iterable<Integer> {
    public final int start;
    public final int end;

    public RangeOld(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException(
                "Start must be less than end"
            );
        }
        this.start = start;
        this.end = end;
    }

    public RangeOld(int end) {
        this(0, end);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeOld rangeOld = (RangeOld) o;
        return start == rangeOld.start && end == rangeOld.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override public String toString() {
        return STR."Range[start=\{start}, end=\{end}]";
    }

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

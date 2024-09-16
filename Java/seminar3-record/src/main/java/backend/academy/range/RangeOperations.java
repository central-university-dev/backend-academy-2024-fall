package backend.academy.range;

public class RangeOperations {

    public static void main(String[] args) {

        Range range = new Range(1, 10);

        for (int value : range) {
            System.out.print(value);
        }

    }
}

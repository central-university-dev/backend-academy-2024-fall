package backend.academy.seminar4.nullsSimple;

public class Divisor {

    /**
     * Divides a by b
     * @param a dividend
     * @param b divisor
     * @return a divided by b. If b is 0, returns null
     */
    public Double divide(Double a, Double b) {
        if (b != 0) {
            return a / b;
        } else {
            return null;
        }
    }
}

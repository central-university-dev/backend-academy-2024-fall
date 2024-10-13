package backend.academy.seminar4.nullsSimple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NpeExample {

    private static final Logger log = LogManager.getLogger(NpeExample.class);

    public static void main(String[] args) {
        Divisor d = new Divisor();
        boolean eq1 = d.divide(1d,2d).equals(d.divide(1d,2d));
        boolean eq2 = d.divide(1d,0d).equals(d.divide(1d,0d));

        log.info(eq1);
        log.info(eq2);

        // Need to wrap all possible null into an if
        Double d1 = d.divide(1d,0d);
        Double d2 = d.divide(1d,0d);
        boolean eq3 = d2 == null;
        if (d1 != null) {
            eq3 = d1.equals(d2);
        }

    }
}

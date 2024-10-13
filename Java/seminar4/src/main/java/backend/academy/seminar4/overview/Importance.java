package backend.academy.seminar4.overview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Importance {

    private static final Logger log = LogManager.getLogger(Importance.class);

    public static void main(String[] args) {
        String myPrettyNumberStr = "1";
        log.info(Integer.parseInt(myPrettyNumberStr));

        String myIncorrectNumberStr = "a";
        log.info(Integer.parseInt(myIncorrectNumberStr));

        log.info("Very important line of code, that must be executed");
    }
}

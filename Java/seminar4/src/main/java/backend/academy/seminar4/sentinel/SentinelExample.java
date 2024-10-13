package backend.academy.seminar4.sentinel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SentinelExample {

    private static final Logger log = LogManager.getLogger(SentinelExample.class);

    public int findStringInArray(String whatToFind, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            if (whatToFind.equals(strings[i])) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SentinelExample se = new SentinelExample();
        int idx = se.findStringInArray("a", new String[] {"aba", "ab", "a"});
        if (idx >= 0) {
            log.info("Found");
        }
    }
}

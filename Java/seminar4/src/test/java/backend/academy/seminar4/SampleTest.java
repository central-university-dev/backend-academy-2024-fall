package backend.academy.seminar4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class SampleTest {
    private static final Logger log = LogManager.getLogger(SampleTest.class);
    private static final org.slf4j.Logger log2 = LoggerFactory.getLogger(SampleTest.class);

    @Test
    void sampleTest() {
        log.debug("sample log");
        log2.debug("sample log");

        Assertions.assertTrue(true);
    }
}

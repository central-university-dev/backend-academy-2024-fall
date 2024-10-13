package backend.academy.seminar4.stacktrace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceExample {

    private static final Logger log = LogManager.getLogger(StackTraceExample.class);

    public static void main(String[] args) {

        String stacktrace = extractExceptionTraceForAnalysis();
        log.info(stacktrace);

    }

    private static String extractExceptionTraceForAnalysis() {
        try {
            Integer.parseInt("ab");
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.toString();
        }
        return "No trace";
    }
}

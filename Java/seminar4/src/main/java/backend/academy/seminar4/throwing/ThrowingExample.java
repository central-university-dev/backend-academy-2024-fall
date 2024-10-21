package backend.academy.seminar4.throwing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ThrowingExample {

    private static final Logger log = LogManager.getLogger(ThrowingExample.class);

    public int parseInt(String intStr) {
        return Integer.parseInt(intStr);
    }

    public int parseIntWithMessagesLogException(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            String msg = "Can't parse \"" + intStr + "\" to integer value";
            log.error(msg, e);
            return 0;
        }
    }

    public int parseIntWithMessagesThrowException(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            String msg = "Can't parse \"" + intStr + "\" to integer value";
            throw new IllegalArgumentException(msg, e);
        }
    }

    public void validateStringForParsingToInt(String intStr) {
        if (!intStr.matches("[0-9]+")) {
            throw new ValidationException("Passed string is not a number");
        }
    }

    // bad example - return result through exception
    // https://medium.com/@rafacdelnero/11-mistakes-java-developers-make-when-using-exceptions-af481a153397
    public void findStringInArray(String whatToFind, String[] strings) {
        boolean result = false;
        for (int i = 0; i < strings.length; i++) {
            result = result || whatToFind.equals(strings[i]);
        }
        if (!result) {
            throw new ValidationException("String not found");
        }
    }

    // bad example - doing heavy operations when is able to fail before them
    public void notFailFast() throws InterruptedException {
        // some validation logic in the beginning of the method
        boolean isValid = false;

        // some heavy operations
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
        }


        if (isValid) {
            throw new RuntimeException("Validation error");
        }
    }

    // how to refactor - doing heavy operations when is able to fail before them
    public void failFast() throws InterruptedException {
        // some validation logic in the beginning of the method
        boolean isValid = false;

        if (isValid) {
            throw new RuntimeException("Validation error");
        }

        // some heavy operations
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
        }
    }

    public void errorCollection() {
        List<String> elements = new ArrayList<>();
        elements.add("a");
        elements.add("ab");
        elements.add("aba");
        elements.add("abac");

        List<Boolean> notOkConversions =
            elements.stream().map((s) -> s.equalsIgnoreCase("AB")).filter((b) -> !b).toList();

        if (!notOkConversions.isEmpty()) {
            throw new RuntimeException("Validation error");
        }
    }

    public static void main(String[] args) {
        ThrowingExample te = new ThrowingExample();
//        te.parseInt("ab");
//        log.info(te.parseIntWithMessagesLogException("ab"));
//        log.info(te.parseIntWithMessagesThrowException("ab"));
//        te.validateStringForParsingToInt("df");

        te.errorCollection();
    }

}

package backend.academy.seminar6.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexExample {

    private static final Logger logger = LoggerFactory.getLogger(RegexExample.class);

    public static void main(String[] args) {
        basicExample();
        iterationExample();
        isValidEmail("email@example.com"); // is valid: true,
        // also: Local: email, Domain: example, Top-Level: com
        isValidEmail("not-email@com"); // is valid: false
        patternWithFlags();
    }

    public static void basicExample() {
        String helloWorld = "Hello World!";
        String welcomeToJava = "Welcome to Java!";

        Pattern regex = Pattern.compile("World");

        Matcher helloWorldMatcher = regex.matcher(helloWorld);
        logger.info("Find in helloWorld result: {}", helloWorldMatcher.find()); // true

        Matcher welcomeToJavaMatcher = regex.matcher(welcomeToJava);
        logger.info("Find in welcomeToJava result: {}", welcomeToJavaMatcher.find()); // false

        String justWorld = "World";
        Matcher justWorldMatcher = regex.matcher(justWorld);
        logger.info("Matches in justWorld result: {}", justWorldMatcher.matches()); // true

        logger.info("Matches in helloWorld result: {}", helloWorldMatcher.matches()); // false
    }

    public static void iterationExample() {
        String str = "Every language in the world begins from the \"Hello World\"";
        Pattern regex = Pattern.compile("[Ww]orld"); // stateless
        Matcher matcher = regex.matcher(str); // stateful
        while (matcher.find()) {
            // (22, 27), (51, 56)
            logger.info(
                "World fount at ({}, {})",
                matcher.start(), matcher.end()
            );
        }
    }

    // Good practice to compile a pattern into a static final field
    private static final Pattern EMAIL_REGEX = Pattern.compile("^([\\w-.]+)@([\\w-]+)\\.+([\\w-]{2,4})$");

    public static void isValidEmail(String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        boolean isValid = matcher.matches(); // find() will also match entire string
        logger.info("Email {} is valid: {}", email, isValid);
        if (isValid) {
            logger.info(
                "Local: {}, Domain: {}, Top-Level: {}",
                matcher.group(1), matcher.group(2), matcher.group(3)
            );
        }
    }

    public static void patternWithFlags() {
        String multilineString = """
            First line
            Second line
            Third line
            second line... oh, no! The fourth line""";

        Pattern pattern = Pattern.compile(
            "^second", // or "(?ium)^second" without bitmask
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
        );
        Matcher matcher = pattern.matcher(multilineString);
        while (matcher.find()) {
            // Second within (11, 17)
            // second within (34, 40)
            logger.info("{} within ({}, {})", matcher.group(), matcher.start(), matcher.end());
        }
    }
}

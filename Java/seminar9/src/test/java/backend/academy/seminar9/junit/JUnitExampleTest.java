package backend.academy.seminar9.junit;

import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JUnitExampleTest {

    private static final Logger logger = LoggerFactory.getLogger(JUnitExampleTest.class);

    @Test
    void exampleTest() {
        int input = 3;
        int result = input + 5;
        assertEquals(8, result);
    }

    @Test
    void assertionsExample() {
        assertInstanceOf(Number.class, 0.34d);

        assertTrue(0 <= 10);

        assertThrows(NullPointerException.class, () -> {
            String nullString = null;
            nullString.toUpperCase();
        });

        String result = assertDoesNotThrow(() -> "first" + "second");

        assertNotNull(result);

        assertAll(
            () -> assertArrayEquals(new int[] {1, 2}, new int[] {1, 2}),
            () -> assertIterableEquals(List.of(1, 2), new LinkedHashSet<>(List.of(1, 2)))
        );
    }

    @BeforeAll
    static void beforeAll() {
        logger.info("Before all @Test methods in class");
    }

    @BeforeEach
    void beforeEach() {
        logger.info("Before each @Test method");
    }

    @AfterEach
    void afterEach() {
        logger.info("After each @Test method");
    }

    @AfterAll
    static void afterAll() {
        logger.info("After all @Test methods in class");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void parameterizedTest(int input) {
        assertTrue(input > 0);
    }

    @ParameterizedTest
    @MethodSource("intsFromMethod")
    void methodSourceParameterizedTest(int input) {
        assertTrue(input > 0);
    }

    private static List<Integer> intsFromMethod() {
        return List.of(1, 2, 3);
    }
}

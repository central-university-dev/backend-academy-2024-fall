package backend.academy.seminar9.coffee.badtests;

import backend.academy.seminar9.coffee.completed.BeverageType;
import backend.academy.seminar9.coffee.completed.CoffeeBeverage;
import backend.academy.seminar9.coffee.completed.DecalcDetector;
import java.util.Random;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BadExamplesTest {

    @Test
    @Disabled("Improper usage of assertions methods")
    void improperAssertsTest() {
        // Use proper assertions. assertNotEquals is better for that case
        assertTrue(0 != 5);

        String result = "Hello, " + "World!";
        // Expected and actual values are reversed
        assertEquals(result, "Hello, World!");

        // Do not use complex logic in assertions
        assertTrue(result.equals("Hello, World!") && result.length() == 13);

        // Always specify delta when comparing floats or doubles
        assertNotEquals(0.1f, 1.0f - 0.9f);
        assertEquals(0.1f, 1.0f - 0.9f, 0.0001f);

        // Always notice boxing. Some versions of junit may fail to compare boxed and unboxed values
        assertEquals(Integer.valueOf(23), 23);
    }

    @Test
    @Disabled("Do not use Random for inputs – hardcode them")
    void randomValuesTest() {
        var random = new Random();
        int threshold = random.nextInt(0, Integer.MAX_VALUE);

        var detector = new DecalcDetector(threshold);

        detector.incrementCounter();
        assertFalse(detector.isDecalcificationRequired());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    @Disabled("Do not use if-else expressions – write a separate test for every use case")
    void ifElseTest(int increment) {
        var detector = new DecalcDetector(Integer.MAX_VALUE);
        if (increment < 0) {
            assertThrows(IllegalArgumentException.class, () -> detector.incrementCounter(increment));
        } else if (increment == 0) {
            int currentCounter = detector.getCounter();
            detector.incrementCounter(increment);
            assertEquals(currentCounter, detector.getCounter());
        } else {
            int currentCounter = detector.getCounter();
            detector.incrementCounter(increment);
            assertEquals(currentCounter + increment, detector.getCounter());
        }
    }

    @ParameterizedTest
    @EnumSource(BeverageType.class)
    @Disabled("Do not use switch expressions – write a separate test for every case")
    void switchExpressionsTest(BeverageType type) {
        CoffeeBeverage beverage = switch (type) {
            case ESPRESSO -> new CoffeeBeverage(1, 0, 0);
            case AMERICANO -> new CoffeeBeverage(1, 2, 0);
            case CAPPUCCINO -> new CoffeeBeverage(1, 0, 4);
            case LATTE -> new CoffeeBeverage(1, 0, 6);
        };

        type.isCanonical(beverage);
    }

}

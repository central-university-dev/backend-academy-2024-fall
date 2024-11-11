package backend.academy.seminar9.coffee.completed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecalcDetectorTest {

    @Test
    @DisplayName("Счётчик детектора корректно инкрементируется на 1")
    void shouldIncrementCounter() {
        var decalcDetector = new DecalcDetector(Integer.MAX_VALUE);

        decalcDetector.incrementCounter();
        assertEquals(1, decalcDetector.getCounter());
    }

    @Test
    @DisplayName("Счётчик детектора корректно инкрементируется на произвольное значение")
    void shouldIncrementCounterBySpecifiedValue() {
        var decalcDetector = new DecalcDetector(Integer.MAX_VALUE);

        decalcDetector.incrementCounter(5);
        assertEquals(5, decalcDetector.getCounter());
    }

    @Test
    @DisplayName("Выбрасывается IllegalArgumentException, если запрошен инкремент на отрицательное значение")
    void shouldNotIncrementCounterByNegativeValue() {
        var decalcDetector = new DecalcDetector(Integer.MAX_VALUE);

        assertThrows(IllegalArgumentException.class, () -> decalcDetector.incrementCounter(-5));
    }

    @Test
    @DisplayName("Счётчик детектора корректно обнуляется")
    void shouldResetCounter() {
        var decalcDetector = new DecalcDetector(Integer.MAX_VALUE);

        decalcDetector.incrementCounter(1835789);
        assertNotEquals(0, decalcDetector.getCounter());

        decalcDetector.reset();
        assertEquals(0, decalcDetector.getCounter());
    }

    @ParameterizedTest
    @ValueSource(ints = {-6173576, 0})
    @DisplayName("Выбрасывается IllegalArgumentException при создании детектора с отрицательным или нулевым предельным значением счётчика")
    void shouldNotCreateDetectorWithThresholdLessThanZero(int threshold) {
        assertThrows(IllegalArgumentException.class, () -> new DecalcDetector(threshold));
    }

    @Test
    @DisplayName("Детектор возвращает false при проверке декальцинации, если счётчик не достиг предельного значения")
    void shouldNotRequireDecalcificationBeforeThreshold() {
        int threshold = 3;
        var decalcDetector = new DecalcDetector(threshold);

        decalcDetector.incrementCounter();
        assertFalse(decalcDetector.isDecalcificationRequired());
    }

    @Test
    @DisplayName("Детектор возвращает true при проверке декальцинации, если счётчик достиг предельного значения")
    void shouldRequireDecalcificationAfterThreshold() {
        int threshold = 10;
        var decalcDetector = new DecalcDetector(threshold);

        decalcDetector.incrementCounter(threshold);
        assertTrue(decalcDetector.isDecalcificationRequired());

        decalcDetector.incrementCounter();
        assertTrue(decalcDetector.isDecalcificationRequired());
    }

    @Test
    @DisplayName("Детектор возвращает true при проверке декальцинации, если после инкремента произошло переполнение разрядной сетки счётчика")
    void shouldHandleCounterWithIntegerOverflow() {
        var decalcDetector = new DecalcDetector(7485);

        decalcDetector.incrementCounter(Integer.MAX_VALUE);
        decalcDetector.incrementCounter();
        assertTrue(decalcDetector.isDecalcificationRequired());
    }
}

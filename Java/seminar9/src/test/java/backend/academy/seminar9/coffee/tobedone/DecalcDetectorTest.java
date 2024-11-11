package backend.academy.seminar9.coffee.tobedone;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DecalcDetectorTest {

    @Test
    @DisplayName("Счётчик детектора корректно инкрементируется на 1")
    @Disabled("Remove this to actually test")
    void shouldIncrementCounter() {

    }

    @Test
    @DisplayName("Счётчик детектора корректно инкрементируется на произвольное значение")
    @Disabled("Remove this to actually test")
    void shouldIncrementCounterBySpecifiedValue() {

    }

    @Test
    @DisplayName("Выбрасывается IllegalArgumentException, если запрошен инкремент на отрицательное значение")
    @Disabled("Remove this to actually test")
    void shouldNotIncrementCounterByNegativeValue() {

    }

    @Test
    @DisplayName("Счётчик детектора корректно обнуляется")
    @Disabled("Remove this to actually test")
    void shouldResetCounter() {

    }

    @ParameterizedTest
    @ValueSource(ints = {})
    @DisplayName("Выбрасывается IllegalArgumentException при создании детектора с отрицательным или нулевым предельным значением счётчика")
    @Disabled("Remove this to actually test")
    void shouldNotCreateDetectorWithThresholdLessThanZero(int threshold) {

    }

    @Test
    @DisplayName("Детектор возвращает false при проверке декальцинации, если счётчик не достиг предельного значения")
    @Disabled("Remove this to actually test")
    void shouldNotRequireDecalcificationBeforeThreshold() {

    }

    @Test
    @DisplayName("Детектор возвращает true при проверке декальцинации, если счётчик достиг предельного значения")
    @Disabled("Remove this to actually test")
    void shouldRequireDecalcificationAfterThreshold() {

    }

    @Test
    @DisplayName("Детектор возвращает true при проверке декальцинации, если после инкремента произошло переполнение разрядной сетки счётчика")
    @Disabled("Remove this to actually test")
    void shouldHandleCounterWithIntegerOverflow() {

    }
}

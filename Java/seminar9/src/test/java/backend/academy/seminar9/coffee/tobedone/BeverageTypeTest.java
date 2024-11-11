package backend.academy.seminar9.coffee.tobedone;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeverageTypeTest {

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется null")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalIfBeverageIsNull() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с нулевым содержимым")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalIfBeverageParamsAreEmpty() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом эспрессо")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalWithNegativeCoffee() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом воды")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalWithNegativeWater() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом эспрессо и воды")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalWithNegativeCoffeeAndWater() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом молока")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalWithNegativeMilk() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом эспрессо и молока")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalWithNegativeCoffeeAndMilk() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если в результате проверки объема переполняется разрядная сетка")
    @Disabled("Remove this to actually test")
    void shouldGetNotCanonicalWithIntOverflow() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный эспрессо")
    @Disabled("Remove this to actually test")
    void shouldGetCanonicalEspresso() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный американо")
    @Disabled("Remove this to actually test")
    void shouldGetCanonicalAmericano() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный капучино")
    @Disabled("Remove this to actually test")
    void shouldGetCanonicalCappuccino() {

    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный латте")
    @Disabled("Remove this to actually test")
    void shouldGetCanonicalLatte() {

    }
}

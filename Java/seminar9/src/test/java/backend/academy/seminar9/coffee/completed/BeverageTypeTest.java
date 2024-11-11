package backend.academy.seminar9.coffee.completed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeverageTypeTest {

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется null")
    void shouldGetNotCanonicalIfBeverageIsNull() {
        var beverageType = BeverageType.AMERICANO;
        assertFalse(beverageType.isCanonical(null));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с нулевым содержимым")
    void shouldGetNotCanonicalIfBeverageParamsAreEmpty() {
        var beverageType = BeverageType.AMERICANO;
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(0, 0, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом эспрессо")
    void shouldGetNotCanonicalWithNegativeCoffee() {
        var beverageType = BeverageType.AMERICANO;
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(-1, 2, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом воды")
    void shouldGetNotCanonicalWithNegativeWater() {
        var beverageType = BeverageType.AMERICANO;
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(1, -2, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом эспрессо и воды")
    void shouldGetNotCanonicalWithNegativeCoffeeAndWater() {
        var beverageType = BeverageType.AMERICANO;
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(-1, -2, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом молока")
    void shouldGetNotCanonicalWithNegativeMilk() {
        var beverageType = BeverageType.CAPPUCCINO;
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(1, 0, -4)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если проверяется напиток с отрицательным объёмом эспрессо и молока")
    void shouldGetNotCanonicalWithNegativeCoffeeAndMilk() {
        var beverageType = BeverageType.CAPPUCCINO;
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(-1, 0, -4)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает false, если в результате проверки объема переполняется разрядная сетка")
    void shouldGetNotCanonicalWithIntOverflow() {
        var beverageType = BeverageType.AMERICANO;
        int coffee = Integer.MAX_VALUE / 2 + 1; // 1073741824
        int water = Integer.MIN_VALUE; // -2147483648
        // coffee * 2 = -2147483648. If water in coffee = -2147483648, the result must be 'false' anyway
        assertFalse(beverageType.isCanonical(new CoffeeBeverage(coffee, water, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный эспрессо")
    void shouldGetCanonicalEspresso() {
        var beverageType = BeverageType.ESPRESSO;
        assertTrue(beverageType.isCanonical(new CoffeeBeverage(1, 0, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный американо")
    void shouldGetCanonicalAmericano() {
        var beverageType = BeverageType.AMERICANO;
        assertTrue(beverageType.isCanonical(new CoffeeBeverage(1, 2, 0)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный капучино")
    void shouldGetCanonicalCappuccino() {
        var beverageType = BeverageType.CAPPUCCINO;
        assertTrue(beverageType.isCanonical(new CoffeeBeverage(1, 0, 4)));
    }

    @Test
    @DisplayName("Проверка каноничности возвращает true, если передан каноничный латте")
    void shouldGetCanonicalLatte() {
        var beverageType = BeverageType.LATTE;
        assertTrue(beverageType.isCanonical(new CoffeeBeverage(1, 0, 6)));
    }
}

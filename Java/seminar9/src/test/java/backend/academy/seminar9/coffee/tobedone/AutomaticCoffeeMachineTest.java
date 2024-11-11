package backend.academy.seminar9.coffee.tobedone;

import backend.academy.seminar9.coffee.withbugs.BeverageType;
import backend.academy.seminar9.coffee.withbugs.CoffeeMachineInventory;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

public class AutomaticCoffeeMachineTest {

    @ParameterizedTest
    @CsvSource({})
    @DisplayName("Выбрасывается IllegalArgumentException, если запрошено приготовление напитка с отрицательными параметрами")
    @Disabled("Remove this to actually test")
    void shouldNotBrewCustomWithNegativeParams(int coffee, int water, int milk) {

    }

    @Test
    @DisplayName("При приготовлении напитка с произвольными параметрами расход запасов кофемашины вычисляется корректно")
    @Disabled("Remove this to actually test")
    void shouldBrewCustomBeverage() {

    }

    @ParameterizedTest
    @EnumSource(BeverageType.class)
    @DisplayName("При приготовлении напитка указанного типа возвращается канонический кофейный напиток")
    @Disabled("Remove this to actually test")
    void shouldBrewCanonicalBeverages(BeverageType beverageType) {

    }

    @Test
    @DisplayName("Выбрасывается DecalcificationRequiredException, если требуется декальцинация кофемашины")
    @Disabled("Remove this to actually test")
    void shouldNotBrewIfDecalcificationRequired() {

    }

    @Test
    @DisplayName("Можно приготовить кофейный напиток после проведения декальцинации кофемашины")
    @Disabled("Remove this to actually test")
    void shouldBrewAfterMaintenance() {

    }

    @ParameterizedTest
    @MethodSource("getNotEnoughSuppliesInventory")
    @DisplayName("Выбрасывается NotEnoughSuppliesException c корректным сообщением, если каких-то из запасов кофемашины не хватает для приготовления напитка")
    @Disabled("Remove this to actually test")
    void shouldNotBrewIfNotEnoughSupplies(
        BeverageType beverageType,
        CoffeeMachineInventory inventory,
        String expectedMessage
    ) {

    }

    private static Stream<Arguments> getNotEnoughSuppliesInventory() {
        return Stream.of();
    }
}

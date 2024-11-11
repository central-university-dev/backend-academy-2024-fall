package backend.academy.seminar9.coffee.completed;

import static java.lang.Math.multiplyExact;

/**
 * Type of the coffee beverage. Possible types:
 * <ul>
 *     <li>Americano: 1 volume of espresso to 2 volumes of water.</li>
 *     <li>Cappuccino: 1 volume of espresso to 4 volumes of milk.</li>
 *     <li>Latte: 1 volume of espresso to 6 volumes of milk.</li>
 * </ul>
 */
public enum BeverageType {
    ESPRESSO(1, 0, 0),
    AMERICANO(1, 2, 0),
    CAPPUCCINO(1, 0, 4),
    LATTE(1, 0, 6);

    private final int espressoVolumes;
    private final int waterVolumes;
    private final int milkVolumes;

    BeverageType(int espressoVolumes, int waterVolumes, int milkVolumes) {
        this.espressoVolumes = espressoVolumes;
        this.waterVolumes = waterVolumes;
        this.milkVolumes = milkVolumes;
    }

    /**
     * Check if the given coffee beverage is canonical,
     * i.e. it has specific proportions of coffee, water and milk
     *
     * @param coffeeBeverage coffee beverage to check
     * @return {@code true}, if given coffee beverage is canonical
     */
    public boolean isCanonical(CoffeeBeverage coffeeBeverage) {
        if (coffeeBeverage == null) {
            return false;
        }
        if (coffeeBeverage.coffee() <= 0) {
            return false;
        }

        try {
            int coffee = multiplyExact(coffeeBeverage.coffee(), espressoVolumes);
            return multiplyExact(coffee, waterVolumes) == coffeeBeverage.water()
                && multiplyExact(coffee, milkVolumes) == coffeeBeverage.milk();
        } catch (ArithmeticException e) {
            return false;
        }
    }
}

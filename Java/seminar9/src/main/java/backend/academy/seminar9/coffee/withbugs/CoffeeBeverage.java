package backend.academy.seminar9.coffee.withbugs;

/**
 * Coffee beverage of some type
 *
 * @param coffee amount of coffee itself in milliliters in a beverage (espresso)
 * @param water  amount of extra water in milliliters in a beverage (mainly for americano, which is espresso + extra water)
 * @param milk   amount of milk in milliliters in a beverage
 */
public record CoffeeBeverage(
    int coffee,
    int water,
    int milk
) {
}

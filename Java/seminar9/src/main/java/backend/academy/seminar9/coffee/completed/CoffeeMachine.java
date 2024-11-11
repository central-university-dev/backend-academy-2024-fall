package backend.academy.seminar9.coffee.completed;

public interface CoffeeMachine {

    CoffeeBeverage brewCoffee(BeverageType beverageType);

    CoffeeBeverage brewCustom(int coffee, int water, int milk);

}

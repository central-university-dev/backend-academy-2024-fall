package backend.academy.seminar9.coffee.tdd;

import backend.academy.seminar9.coffee.completed.BeverageType;
import backend.academy.seminar9.coffee.completed.CoffeeBeverage;
import backend.academy.seminar9.coffee.completed.CoffeeMachine;
import backend.academy.seminar9.coffee.completed.CoffeeMachineInventory;
import backend.academy.seminar9.coffee.completed.DecalcDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automatic coffee machine with supplies storage: coffee beans, water and milk.
 * <ul>
 *     <li>1 portion of 30ml espresso liquid requires 15g of beans + 30ml water.</li>
 *     <li>1g of coffee bean absorbs 1ml of water while brewing the coffee.</li>
 *     <li>Thus, 30ml of espresso will end up using 15g of beans + 45ml of water.</li>
 *     <li>1ml of water is required for every 20ml of milk for steam generation.</li>
 * </ul>
 */
public class AutomaticCoffeeMachine implements CoffeeMachine {

    private static final Logger logger = LoggerFactory.getLogger(AutomaticCoffeeMachine.class);

    private final CoffeeMachineInventory inventory;
    private final DecalcDetector decalcDetector;

    public AutomaticCoffeeMachine(CoffeeMachineInventory inventory, DecalcDetector decalcDetector) {
        this.inventory = inventory;
        this.decalcDetector = decalcDetector;
    }

    @Override
    public CoffeeBeverage brewCoffee(BeverageType beverageType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public CoffeeBeverage brewCustom(int coffee, int water, int milk) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}

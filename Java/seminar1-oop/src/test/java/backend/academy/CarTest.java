package backend.academy;

import backend.academy.cars.Car;
import backend.academy.cars.LightWeightCar;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarTest {

    @Test
    void name() {
        Car car = new LightWeightCar("BMW", "X5", 2015, "Black");
        Car car2 = new LightWeightCar("AUDI", "X6", 2015, "White");
        Car car3 = new LightWeightCar("LADA", "X7", 1997, "Green");

        car.startEngine();
        car2.startEngine();
        car3.startEngine();


        assertNotNull(car, "Audi");
        assertNotNull(car2, "Audi");
        assertNotNull(car3, "Audi");
    }
}

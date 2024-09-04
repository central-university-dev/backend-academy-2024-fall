package backend.academy.cars;

public class ElectricCar extends Car {
    public ElectricCar() {
        super("Tesla", "Model S", 2020, "Black");
    }

    @Override
    public void startEngine() {
        if(fuelLevel >= 0) {
            super.startEngine();
        }
    }
}

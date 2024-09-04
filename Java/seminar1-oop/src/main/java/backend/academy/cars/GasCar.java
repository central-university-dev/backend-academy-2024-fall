package backend.academy.cars;

public class GasCar extends Car {
    public GasCar() {
        super("Gasoline Car", "Gasoline", 20013, "Brown");
    }

    @Override
    public void startEngine() {
        System.out.println("Starting gasoline engine with a roar...");
    }
}

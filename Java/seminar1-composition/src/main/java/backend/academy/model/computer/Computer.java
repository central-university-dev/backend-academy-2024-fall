package backend.academy.model.computer;

import backend.academy.validators.HardwareValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Computer implements Serializable {
    private final List<Component> components = new ArrayList<>();

    private final HardwareValidator validator = new HardwareValidator();

    public void addComponent(Component component) throws Exception {
        String result = validator.validate(components);
        if (result == null || result.isEmpty()) {
            components.add(component);
        } else {
            System.out.println(
                STR."Component \{component.getName()} was not added because of the following reasons:\n\{result}"
            );
        }
    }

    public double getTotalPrice() {
        double sum = 0.0;
        for (Component component : components) {
            double price = component.getPrice();
            sum += price;
        }
        return sum;
    }

    public void printSpecs() {
        for (Component component : components) {
            System.out.println(component.getSpecs());
        }
        System.out.println("Total Price: $" + getTotalPrice());
    }

    public String getSpecs() {
        StringJoiner joiner = new StringJoiner("\n");
        for (Component component : components) {
            String specs = component.getSpecs();
            joiner.add(specs);
        }
        return joiner.toString();
    }

}

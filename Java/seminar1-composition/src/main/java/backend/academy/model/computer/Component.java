package backend.academy.model.computer;

// Make Component class implement Serializable
public abstract class Component {
    private final String name;
    private final double price;

    public Component(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getType();

    public abstract String getSpecs();
}

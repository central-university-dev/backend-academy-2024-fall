package backend.academy.model.computer;

public class GPU extends Component {
    public static final String GPU = "GPU";
    private int memory;

    public GPU(String name, double price, int memory) {
        super(name, price);
        this.memory = memory;
    }

    @Override
    public String getType() {
        return GPU;
    }

    @Override
    public String getSpecs() {
        return "GPU: " + getName() + ", " + memory + " GB";
    }
}

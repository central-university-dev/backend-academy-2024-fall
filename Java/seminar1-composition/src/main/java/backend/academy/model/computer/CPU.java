package backend.academy.model.computer;

// Derived classes for specific components
public class CPU extends Component {
    public static final String TYPE = "CPU";
    private int cores;
    private double frequency;
    private final String socketType;

    public CPU(String name, double price, int cores, double frequency, String socketType) {
        super(name, price);
        this.cores = cores;
        this.frequency = frequency;
        this.socketType = socketType;
    }

    public String getSocketType() {
        return socketType;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getSpecs() {
        return "CPU: " + getName() + ", " + cores + " cores @ " + frequency + " GHz, Socket: " + socketType;
    }
}

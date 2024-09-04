package backend.academy.model.computer;

public class Storage extends Component {
    public static final String TYPE = "STORAGE";
    private int capacity;

    public Storage(String name, double price, int capacity) {
        super(name, price);
        this.capacity = capacity;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getSpecs() {
        return "Storage: " + getName() + ", " + capacity + " GB";
    }
}

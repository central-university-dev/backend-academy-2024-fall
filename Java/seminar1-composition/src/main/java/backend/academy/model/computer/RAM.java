package backend.academy.model.computer;

public class RAM extends Component {
    public static final String TYPE = "RAM";
    private int size;

    public RAM(String name, double price, int size) {
        super(name, price);
        this.size = size;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getSpecs() {
        return "RAM: " + getName() + ", " + size + " GB";
    }
}

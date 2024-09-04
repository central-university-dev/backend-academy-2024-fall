package backend.academy.model.computer;

public class PowerUnit extends Component {
    public static final String POWER_UNIT = "POWER_UNIT";
    private int wattage;

    public PowerUnit(String name, double price, int wattage) {
        super(name, price);
        this.wattage = wattage;
    }

    @Override
    public String getType() {
        return POWER_UNIT;
    }

    @Override
    public String getSpecs() {
        return "Power Unit: " + getName() + ", " + wattage + "W";
    }
}

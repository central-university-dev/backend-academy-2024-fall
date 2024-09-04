package backend.academy.model.computer;

import java.util.ArrayList;
import java.util.List;

public class Motherboard extends Component {
    public static final String TYPE = "MB";
    private final String socketType;
    private final int maxRamSlots;

    public Motherboard(
        String name,
        double price,
        String socketType,
        int maxRamSlots
    ) {
        super(name, price);
        this.socketType = socketType;
        this.maxRamSlots = maxRamSlots;
    }

    public String getSocketType() {
        return socketType;
    }

    public int getMaxRamSlots() {
        return maxRamSlots;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getSpecs() {
        return STR."Motherboard{name=\{getName()}, socketType='\{socketType}', maxRamSlots=\{maxRamSlots}}";
    }
}

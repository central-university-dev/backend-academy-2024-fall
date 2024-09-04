package backend.academy.model.computer.cases;

import backend.academy.model.computer.Component;

public abstract class Case extends Component {

    public static final String TYPE = "CASE";
    private final String formFactor;

    public Case(String name, double price, String formFactor) {
        super(name, price);
        this.formFactor = formFactor;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getFormFactor(){
        return formFactor;
    }

    @Override
    public String getSpecs() {
        return STR."Case: \{getName()} | Form Factor: \{getFormFactor()} | Price: $\{getPrice()}";
    }
}

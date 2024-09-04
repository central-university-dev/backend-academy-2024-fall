package backend.academy.model.computer.cases;

import static java.lang.String.format;

public class ATXCase extends Case{

    public static final String FORM_FACTOR = "ATX";

    public ATXCase(String name, double price) {
        super(name, price, FORM_FACTOR);
    }

}

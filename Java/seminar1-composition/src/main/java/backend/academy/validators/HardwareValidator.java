package backend.academy.validators;

import backend.academy.model.computer.CPU;
import backend.academy.model.computer.Component;
import backend.academy.model.computer.Motherboard;
import java.util.List;
import java.util.Objects;

public class HardwareValidator {

    public String validate(List<Component> components) {
        String errorMessage = "";
        String validationResult = validateFormFactor(components);
        if (validationResult != null && !validationResult.isEmpty()) {
            errorMessage += validationResult + "\n";
        }
        validationResult = validateCpuCompatibility(components);
        if (validationResult != null && !validationResult.isEmpty()) {
            errorMessage += validationResult + "\n";
        }
        validationResult = validatePowerConsumption(components);
        if (validationResult != null && !validationResult.isEmpty()) {
            errorMessage += validationResult + "\n";
        }
        return errorMessage;
    }

    //always true - because we don't have implemented power consumption specs yet
    private String validatePowerConsumption(List<Component> components) {
        return null;
    }

    private String validateCpuCompatibility(List<Component> components) {
        String mbSocket = null;
        String cpuSocket = null;
        for (Component component : components) {
            if (Objects.equals(component.getType(), Motherboard.TYPE)) {
                mbSocket = ((Motherboard) component).getSocketType();
            }
            if (Objects.equals(component.getType(), CPU.TYPE)) {
                cpuSocket = ((CPU) component).getSocketType();
            }
        }
        if (mbSocket == null || cpuSocket == null || Objects.equals(mbSocket, cpuSocket)) {
            return null;
        } else {
            return STR."Incompatible components! Different socket types. MB has \{mbSocket} and CPU has \{cpuSocket}";
        }
    }

    private String validateFormFactor(List<Component> components) {
        return null;
    }

}

package backend.academy;

import backend.academy.model.computer.CPU;
import backend.academy.model.computer.Computer;
import backend.academy.model.computer.GPU;
import backend.academy.model.computer.Motherboard;
import backend.academy.model.computer.PowerUnit;
import backend.academy.model.computer.RAM;
import backend.academy.model.computer.Storage;
import backend.academy.service.PersistenceService;

public class Main {
    public static void main(String[] args) {
        try {
            PersistenceService persistenceService = new PersistenceService();

            Computer computer = new Computer();
            computer.addComponent(new Motherboard("ASUS ROG", 200.0, "AM5", 4));
            computer.addComponent(new CPU("Intel i7", 300.0, 8, 3.6, "LGA1151"));
            computer.addComponent(new RAM("Corsair Vengeance", 150.0, 16));
            computer.addComponent(new RAM("Corsair Vengeance", 150.0, 16));
            computer.addComponent(new GPU("NVIDIA GTX 1080", 500.0, 8));
            computer.addComponent(new Storage("Samsung SSD", 200.0, 512));
            computer.addComponent(new PowerUnit("Corsair RM750x", 120.0, 750));

            computer.printSpecs();

            // Save configuration to a file
            //persistenceService.saveToFile(computer, "computer_config.dat");

            // Load configuration from a file
            //Computer loadedComputer = persistenceService.loadFromFile("computer_config.dat");
            //System.out.println("\nLoaded Computer Configuration:");
            //loadedComputer.printSpecs();
        } catch (Exception e) {
            System.out.println(STR."Error: \{e.getMessage()}");
        }
    }
}

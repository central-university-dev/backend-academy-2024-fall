package backend.academy.seminar14_example;

import backend.academy.seminar14_example.annotations.Autowired;
import backend.academy.seminar14_example.di.InjectionFactory;
import backend.academy.seminar14_example.model.Message;
import backend.academy.seminar14_example.model.Person;
import backend.academy.seminar14_example.services.MessageService;
import backend.academy.seminar14_example.services.PersonStore;
import backend.academy.seminar14_example.services.commands.CommandHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Scanner;


public class Application {
    public static void main(String[] args) throws IOException, URISyntaxException, IllegalAccessException {
        Application application = new Application();
        InjectionFactory factory = new InjectionFactory(application.getClass().getPackageName());
        factory.instantiateComponents();
        factory.initializeComponents();
        factory.initialize(application);
        factory.registerCommands();
        application.run();
    }

    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private MessageService messageService;
    @Autowired
    private PersonStore personStore;
    @Autowired
    private CommandHandler commandHandler;

    Application() {

    }


    private void run() {
        System.out.println(
            """
                Welcome! It's a simple push service. Type 'help' to get available commands. Type 'exit' to finish.
                """
        );
        String input = null;
        while (!"exit".equalsIgnoreCase(input)) {
            input = scanner.nextLine().trim().toLowerCase();
            commandHandler.executeCommand(input);
        }
        scanner.close();
    }

    private void sendPush(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("message command should have 1 argument: <person name>");
        }
        String name = args[0];
        Optional<Person> person = personStore.getPerson(name);
        Person p = person.orElseThrow(() -> new RuntimeException("Person not found with name: " + name));
        System.out.println("Input the message:");
        String message = scanner.nextLine().trim().toLowerCase();
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message should not be empty!");
        }
        messageService.sendMessage(p, new Message(message));
    }

}

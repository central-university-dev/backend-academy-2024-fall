package backend.academy.seminar14_example.services.commands;

import backend.academy.seminar14_example.annotations.Autowired;
import backend.academy.seminar14_example.model.Person;
import backend.academy.seminar14_example.services.PersonStore;

@CommandDefinition(
    name = "add",
    arguments = "<person name> <email>",
    description = "Create and add a person with given name and email to the person store",
    aliases = {"Add", "New", "new"},
    argsNumber = 2
)
public class AddPersonCommand implements Command {

    @Autowired
    private PersonStore personStore;

    @Override
    public void execute(String... args) {
        personStore.save(new Person(args[0], args[1]));
        System.out.println("Person was successfully added!");
    }
}

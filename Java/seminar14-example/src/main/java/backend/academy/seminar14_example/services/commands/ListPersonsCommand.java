package backend.academy.seminar14_example.services.commands;

import backend.academy.seminar14_example.annotations.Autowired;
import backend.academy.seminar14_example.services.PersonStore;

@CommandDefinition(name = "list", description = "List all persons added to the store")
public class ListPersonsCommand implements Command{
    @Autowired
    private PersonStore personStore;

    @Override
    public void execute(String... args) {
        System.out.println(personStore.getPersons());
    }
}

package backend.academy.seminar14_example.services;

import backend.academy.seminar14_example.annotations.Component;
import backend.academy.seminar14_example.model.Person;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryPersonStore implements PersonStore {

    private final Map<String, Person> store = new HashMap<>();

    @Override
    public Optional<Person> getPerson(String name) {
        return Optional.ofNullable(store.get(name));
    }

    @Override
    public void save(Person p) {
        store.put(p.name(), p);
    }

    @Override
    public List<Person> getPersons() {
        return store.values().stream().toList();
    }
}

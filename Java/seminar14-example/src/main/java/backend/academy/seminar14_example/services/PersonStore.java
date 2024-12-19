package backend.academy.seminar14_example.services;

import backend.academy.seminar14_example.model.Person;
import java.util.List;
import java.util.Optional;

public interface PersonStore {

    Optional<Person> getPerson(String name);

    void save(Person p);

    List<Person> getPersons();
}

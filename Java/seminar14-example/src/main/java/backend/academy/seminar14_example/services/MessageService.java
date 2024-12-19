package backend.academy.seminar14_example.services;

import backend.academy.seminar14_example.model.Message;
import backend.academy.seminar14_example.model.Person;

public interface MessageService {

    void sendMessage(Person person, Message message);

}

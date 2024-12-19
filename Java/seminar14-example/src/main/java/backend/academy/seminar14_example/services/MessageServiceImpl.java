package backend.academy.seminar14_example.services;

import backend.academy.seminar14_example.annotations.Component;
import backend.academy.seminar14_example.model.Message;
import backend.academy.seminar14_example.model.Person;

@Component
public class MessageServiceImpl implements MessageService {



    @Override
    public void sendMessage(Person person, Message message) {
        // BUSINESS LOGIC
        System.out.println("Message was successfully sent");
    }
}

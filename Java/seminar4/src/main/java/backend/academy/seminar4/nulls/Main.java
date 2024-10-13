package backend.academy.seminar4.nulls;

import backend.academy.seminar4.nulls.model.User;
import backend.academy.seminar4.nulls.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        UserService userService = new UserService();

        initializeUsers(userService);

        User user = userService.findUserById(1);

        log.info(user.getEmail());

        User user1 = userService.findUserById(6);

        log.info(user1.getEmail());
    }

    private static void initializeUsers(UserService userService) {
        userService.addUser(new User(1, "Alice", "alice@example.com"));
        userService.addUser(new User(2, "Bob", "bob@example.com"));
        userService.addUser(new User(3, "Charlie", "charlie@example.com"));
        userService.addUser(new User(4, "Diana", "diana@example.com"));
        userService.addUser(new User(5, "Eve", "eve@example.com"));
    }
}

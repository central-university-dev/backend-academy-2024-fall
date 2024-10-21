package backend.academy.seminar5.project.db;

import backend.academy.seminar5.project.db.entities.User;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserDb {
    private static final Map<Long, User> USERS = new HashMap<>(Map.of(
        1L, new User(1, "user1", "email1"),
        2L, new User(2, "user2", "email2"),
        3L, new User(3, "user3", "email3"),
        4L, new User(4, "user4", "email4"),
        5L, new User(5, "user5", "email5")
    ));

    public static User getUserById(long id) {
        User user = USERS.get(id);
        if (user == null) {
            throw new RuntimeException("user not found by id:" + id);
        } else {
            return user;
        }
    }
}

package backend.academy.seminar2.withResult.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import backend.academy.seminar2.withResult.utils.MyOptional;
import backend.academy.seminar2.withResult.model.User;

public class UserService {

    private final Map<Integer, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public MyOptional<User> findUserById(int userId) {
        return MyOptional.ofNullable(users.get(userId));
    }

    public MyOptional<String> getUserEmail(int userId) {
        return findUserById(userId).map(User::getEmail);
    }

    public MyOptional<String> getUserName(int userId) {
        return findUserById(userId).map(User::getName);
    }

    public String findUserEmailOrThrow(int userId) {
        return getUserEmail(userId)
            .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " has no email"));
    }

    public String findUserNameOrDefault(int userId, String defaultName) {
        return getUserName(userId)
            .orElse(defaultName);
    }
}

package backend.academy.seminar2.withOptional.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import backend.academy.seminar2.withOptional.model.User;

public class UserService {

    private final Map<Integer, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public Optional<User> findUserById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public Optional<String> getUserEmail(int userId) {
        return findUserById(userId).map(User::getEmail);
    }

    public Optional<String> getUserName(int userId) {
        return findUserById(userId).map(User::getName);
    }

    /**
     * Получение email или выброс исключения, если пользователь не найден
     */
    public String findUserEmailOrThrow(int userId) {
        return getUserEmail(userId)
            .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " has no email"));
    }

    /**
     * Получение имени пользователя или значение по умолчанию
     */
    public String findUserNameOrDefault(int userId, String defaultName) {
        return getUserName(userId)
            .orElse(defaultName);
    }
}

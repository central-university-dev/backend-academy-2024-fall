package backend.academy.seminar5.project.analytics.easy._1;

import backend.academy.seminar5.project.db.entities.User;
import java.util.Optional;
import java.util.Random;

public class OptionalIntro {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Optional<User> userOptional;

        userOptional = getSomeUserFromDb();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("user = " + user);
        }

        userOptional.ifPresent(user -> System.out.println("user = " + user));

        /**
         * what if user not found?
         * and we can handle it
         */
        User userFromDb = userOptional.orElse(null);
        System.out.println("userFromDb = " + userFromDb);

        userFromDb = userOptional.orElseGet(() -> userFromSupport());
        System.out.println("userFromDb = " + userFromDb);


        /**
         * what if user not found?
         * and we CANT handle it
         */

        userOptional.orElseThrow(() -> new RuntimeException("user not found in db"));

        userOptional = getSomeUserFromDb();
        if (userOptional.isEmpty()) {
            System.out.println("user not found in db");

            //optional.get();
            //Exception in thread "main" java.util.NoSuchElementException: No value present
        }

    }

    private static Optional<User> getSomeUserFromDb() {
        return RANDOM.nextBoolean()
            ? Optional.of(userFromSupport())
            : Optional.empty();

        // 3 способ создания Optional
        // Optional.ofNullable(null);
    }

    private static User userFromSupport() {
        return new User(1, "support", "help@tbank.ru");
    }
}

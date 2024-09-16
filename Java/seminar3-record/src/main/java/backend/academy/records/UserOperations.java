package backend.academy.records;

public class UserOperations {

    public static void main(String args[]) {
        var user1 = new User(1, "user", "password");
        var user2 = new User(1, "user", "password");

        var id1 = user1.id();
        var name1 = user1.name();
        var password1 = user1.password();

        var hashCode1 = user1.hashCode();
        var hashCode2 = user2.hashCode();

        System.out.println(STR."Are users equal? -\{user1.equals(user2)}");
        System.out.println(STR.
            """
            HashCode1: \{hashCode1},
            HashCode2: \{hashCode2}
            """
        );

        System.out.println(user1.toString());
        System.out.println(user2.toString());
    }
}


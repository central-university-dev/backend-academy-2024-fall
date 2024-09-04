package backend.academy.chess.player;

public class User {
    private long id;
    private String displayName;

    public User(long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public User(User user) {
        this.id = user.id;
        this.displayName = user.displayName;
    }

    public long getId() {
        return id;
    }
}

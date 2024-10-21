package backend.academy.seminar7;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserHttpClientTest {

    private UserHttpServer server;
    private UserHttpClient client;
    private int port;

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        port = TestHelper.getAvailablePort();
        server = new UserHttpServer();
        Executors.newSingleThreadExecutor().submit(() -> server.start(port));
        Thread.sleep(2000);
        client = new UserHttpClient("localhost", port);
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test
    void createUser() {
        // Given
        User user = new User("John Doe", "john@example.com");

        // When
        User createdUser = client.createUser(user);

        // Then
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
    }

    @Test
    void getUser() {
        // Given
        User createdUser = client.createUser(new User("John Doe", "john@example.com"));

        // When
        User receivedUser = client.getUser(createdUser.getId());

        // Then
        assertNotNull(receivedUser);
        assertEquals(createdUser, receivedUser);
    }

    @Test
    void updateUser() {
        // Given
        User createdUser = client.createUser(new User("John Doe", "john@example.com"));
        String newName = "Jane Doe";
        String newEmail = "jane@example.com";

        // When
        User updateUser = client.updateUser(createdUser.getId(), new User(newName, newEmail));

        // Then
        assertNotNull(updateUser);
        assertEquals(createdUser.getId(), updateUser.getId());
        assertEquals(newName, updateUser.getName());
        assertEquals(newEmail, updateUser.getEmail());
    }

    @Test
    void deleteUser() {
        // Given
        User createdUser = client.createUser(new User("John Doe", "john@example.com"));

        // When
        User deleteUser = client.deleteUser(createdUser.getId());

        // Then
        assertNull(deleteUser);
        User receivedUser = client.getUser(createdUser.getId());
        assertNull(receivedUser);
    }
}

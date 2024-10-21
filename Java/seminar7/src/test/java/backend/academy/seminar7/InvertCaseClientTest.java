package backend.academy.seminar7;

import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvertCaseClientTest {

    private InvertCaseServer server;
    private InvertCaseClient client;
    private int port;

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        port = TestHelper.getAvailablePort();
        server = new InvertCaseServer();
        Executors.newSingleThreadExecutor().submit(() -> server.start(port));
        Thread.sleep(2000);
        client = new InvertCaseClient();
        client.startConnection("localhost", port);
    }

    @AfterEach
    public void afterEach() {
        client.stopConnection();
        server.stop();
    }

    @Test
    public void whenClientSendsMessageThenServerReturnsInvertedCase() {
        // Give
        String message = "Hello World!";

        // When
        String response = client.sendMessage(message);

        // Then
        assertEquals("hELLO wORLD!", response);
    }
}

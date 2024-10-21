package backend.academy.seminar7;

import java.io.IOException;
import java.net.ServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHelper {
    private static final Logger logger = LoggerFactory.getLogger(TestHelper.class);

    public static int getAvailablePort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            serverSocket.close();
            return port;
        } catch (IOException e) {
            logger.error("Error when getting availableport", e);
            throw new RuntimeException(e);
        }
    }
}

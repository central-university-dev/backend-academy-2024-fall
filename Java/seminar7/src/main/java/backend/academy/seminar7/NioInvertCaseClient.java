package backend.academy.seminar7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioInvertCaseClient {
    private static final Logger logger = LoggerFactory.getLogger(NioInvertCaseClient.class);

    private static SocketChannel client;
    private static ByteBuffer buffer;

    public void startConnection(String host, int port) {
        try {
            client = SocketChannel.open(new InetSocketAddress(host, port));
            buffer = ByteBuffer.allocate(256);
        } catch (IOException e) {
            logger.error("Error when initializing connection", e);
        }
    }

    public String sendMessage(String message) {
        buffer = ByteBuffer.wrap(message.getBytes());
        String response;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = new String(buffer.array()).trim();
            buffer.clear();
            return response;
        } catch (IOException e) {
            logger.error("Error when sending message", e);
            return null;
        }
    }

    public void stopConnection() {
        try {
            client.close();
        } catch (IOException e) {
            logger.debug("Error when closing connection", e);
        }
    }
}

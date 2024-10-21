package backend.academy.seminar7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioInvertCaseServer {

    private static final Logger logger = LoggerFactory.getLogger(NioInvertCaseServer.class);

    private Selector selector;
    private ServerSocketChannel serverSocket;

    public void start(int port) {
        try {
            logger.info("Server starting on port {}", port);
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectedKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if (key.isAcceptable()) {
                        register(selector, serverSocket);
                    }
                    if (key.isReadable()) {
                        answerWithInvertCase(buffer, key);
                    }
                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            logger.error("Error when starting server", e);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
            selector.close();
        } catch (IOException e) {
            logger.error("Error when stopping server", e);
        }
    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void answerWithInvertCase(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        int r = client.read(buffer);
        if (r == -1) {
            client.close();
        } else {
            buffer.flip();
            invertCase(buffer);
            client.write(buffer);
            buffer.clear();
        }
    }

    private void invertCase(ByteBuffer byteBuffer) {
        for (int x = 0; x < byteBuffer.limit(); x++) {
            byteBuffer.put(x, (byte) invertCase(byteBuffer.get(x)));
        }
    }

    private int invertCase(int data) {
        if (!Character.isLetter(data)) {
            return data;
        }
        if (Character.isUpperCase(data)) {
            return Character.toLowerCase(data);
        } else {
            return Character.toUpperCase(data);
        }
    }

    public static void main(String[] args) {
        NioInvertCaseServer server = new NioInvertCaseServer();
        server.start(8080);
    }
}

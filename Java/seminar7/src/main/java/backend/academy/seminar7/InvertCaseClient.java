package backend.academy.seminar7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InvertCaseClient {
    private static final Logger logger = LoggerFactory.getLogger(InvertCaseClient.class);

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            logger.error("Error when initializing connection", e);
        }
    }

    public String sendMessage(String message) {
        try {
            out.println(message);
            return in.readLine();
        } catch (Exception e) {
            logger.error("Error when sending message", e);
            return null;
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            logger.debug("Error when closing connection", e);
        }
    }
}

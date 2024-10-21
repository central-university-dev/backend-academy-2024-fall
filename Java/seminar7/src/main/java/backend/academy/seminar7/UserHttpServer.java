package backend.academy.seminar7;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserHttpServer {
    private static final Logger logger = LoggerFactory.getLogger(UserHttpServer.class);
    private static final Map<String, User> users = new HashMap<>();
    private static final Gson gson = new Gson();

    private HttpServer server;

    public void start(int port) {
        try {
            logger.info("Server starting on port {}", port);
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/api/v1/users", new UserHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            logger.error("Error when starting server", e);
        }
    }

    public void stop() {
        server.stop(0);
    }

    static class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            logger.info("Received {} request at {}", exchange.getRequestMethod(), path);
            String[] pathParts = path.split("/");
            User response;
            int responseCode = 200;
            try {
                response = switch (exchange.getRequestMethod()) {
                    case "POST" -> {
                        User user = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), User.class);
                        user.setId(UUID.randomUUID().toString());
                        user.setBlocked(false);
                        users.put(user.getId(), user);
                        logger.info("User created: {}", user);
                        responseCode = 201;
                        yield user;
                    }
                    case "PUT" -> {
                        String idToUpdate = pathParts[4];
                        User existingUser = users.get(idToUpdate);
                        if (existingUser != null) {
                            var updatedUser =
                                gson.fromJson(new InputStreamReader(exchange.getRequestBody()), User.class);
                            existingUser.setEmail(updatedUser.getEmail());
                            existingUser.setName(updatedUser.getName());
                            users.put(idToUpdate, existingUser);
                            logger.info("User updated: {}", existingUser);
                            yield existingUser;
                        } else {
                            responseCode = 404;
                            logger.warn("User not found for update: {}", idToUpdate);
                            yield null;
                        }
                    }
                    case "GET" -> {
                        String idToGet = pathParts[4];
                        User foundUser = users.get(idToGet);
                        if (foundUser != null) {
                            logger.info("User retrieved: {}", foundUser);
                            yield foundUser;
                        } else {
                            responseCode = 404;
                            logger.warn("User not found for retrieving: {}", idToGet);
                            yield null;
                        }
                    }
                    case "DELETE" -> {
                        String idToDelete = pathParts[4];
                        User removedUser = users.remove(idToDelete);
                        if (removedUser != null) {
                            logger.info("User deleted: {}", removedUser);
                            yield null;
                        } else {
                            responseCode = 404;
                            logger.warn("User not found for deletion: {}", idToDelete);
                            yield null;
                        }
                    }
                    default -> {
                        responseCode = 405;
                        logger.warn("Method not allowed: {}", exchange.getRequestMethod());
                        yield null;
                    }
                };
            } catch (Exception e) {
                responseCode = 500;
                response = null;
                logger.error("Error processing request", e);
            }
            String rawResponse = response != null ? gson.toJson(response) : "";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseCode, rawResponse.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(rawResponse.getBytes());
            }
        }
    }

    public static void main(String[] args) {
        UserHttpServer server = new UserHttpServer();
        server.start(8080);
    }
}

package backend.academy.seminar7;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class UserHttpClient {
    private final Logger logger = LoggerFactory.getLogger(UserHttpClient.class);

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private final String host;
    private final int port;

    public UserHttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public User createUser(User user) {
        String requestBody = gson.toJson(user);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http", null, host, port, "/api/v1/users", null, null))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();
            HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());
            return handelUserResponse(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error("Error when creating user: {}", user, e);
            throw new RuntimeException(e);
        }
    }

    public User getUser(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http", null, host, port, "/api/v1/users/" + id, null, null))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return handelUserResponse(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error("Error when receiving user by id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

    public User updateUser(String id, User user) {
        try {
            String requestBody = gson.toJson(user);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http", null, host, port, "/api/v1/users/" + id, null, null))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return handelUserResponse(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error("Error when updating user: {}", user, e);
            throw new RuntimeException(e);
        }
    }

    public User deleteUser(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http", null, host, port, "/api/v1/users/" + id, null, null))
                .DELETE()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return handelUserResponse(response);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error("Error when deleting user by id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

    private User handelUserResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        return switch (statusCode) {
            case 200, 201 -> gson.fromJson(response.body(), User.class);
            case 400 -> throw new RuntimeException("Client Error " + statusCode);
            case 404 -> null;
            case 500 -> throw new RuntimeException("Server Error: " + statusCode);
            default -> throw new RuntimeException("Error: " + statusCode);
        };
    }
}


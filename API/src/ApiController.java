import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApiController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); // Get the full path

        switch (requestMethod) {
            case "GET" -> handleGetRequest(exchange, path);
            case "POST" -> handlePostRequest(exchange, path);
            case null, default -> exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }
    }

    private void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        Map<String, String> response = new HashMap<>();

        switch (path) {
            case "/hello" -> response.put("message", "Hello, World!");
            case "/restaurants" -> response.put("restaurants", "List of restaurants"); // TODO: Implement
            default -> response.put("error", "Invalid path");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(response);

        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    /**
     * Handle POST request /restaurants/{restaurantId} with the reservation data in the request body
     */
    private void handlePostRequest(HttpExchange exchange, String path) throws IOException {
        // Read the request body
        Scanner scanner = new Scanner(exchange.getRequestBody()).useDelimiter("\\A");
        String requestBody = scanner.hasNext() ? scanner.next() : "";

        // Handle the path
        int restaurantId = -1;
        if (path.startsWith("/restaurants")) {
            String[] args = path.split("/");

            if (args.length == 3) {
                restaurantId = Integer.parseInt(args[2]);
            }
        }

        Map<String, Object> response = new HashMap<>();
        if (restaurantId == -1) {
            response.put("error", "Invalid path or arguments");
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(404, 0);
        } else {
            // Handle the request body as needed
            // TODO

            response.put("message", "Received POST request");
            response.put("restaurantId", restaurantId);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, 0);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(response);

        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
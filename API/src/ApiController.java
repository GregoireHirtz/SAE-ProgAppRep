import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApiController implements HttpHandler {
    private final String address;

    public ApiController(String address) {
        this.address = address;
    }

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
        Object response;
        ObjectMapper objectMapper = new ObjectMapper();

        if (path.equals("/hello")) {
            response = objectMapper.readValue("{\"message\":\"Hello, World!\"}", HashMap.class);
        }
        else if (path.startsWith("/restaurants")) {
            String[] args = path.split("/");
            if (args.length == 3) {
                response = objectMapper.readValue(ApiService.getInstance(this.address).getRestaurant(Integer.parseInt(args[2])), HashMap.class);
            }
            else if (args.length == 2) {
                response = objectMapper.readValue(ApiService.getInstance(this.address).getRestaurants(), new TypeReference<List<HashMap>>() {});            }
            else {
                response = objectMapper.readValue("{\"error\":\"Invalid path or arguments\"}", HashMap.class);
            }
        }
        else {
            response = objectMapper.readValue("{\"error\":\"Invalid path\"}", HashMap.class);
        }

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
            try {
                HashMap reservation = new ObjectMapper().readValue(requestBody, HashMap.class);
                String nom = (String) reservation.get("nom");
                String prenom = (String) reservation.get("prenom");
                int nbpers = (int) reservation.get("nbpers");
                String telephone = (String) reservation.get("telephone");
                int numrestau = restaurantId;
                Date date = Date.valueOf((String) reservation.get("date"));
                ApiService.getInstance(this.address).reserverTable(nom, prenom, nbpers, telephone, numrestau, date);
            } catch (Exception e) {
                response.put("error", e.getMessage());
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(400, 0);
            }

            response.put("message", "Reservation created successfully");
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
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Api {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);
        server.createContext("/hello", new ApiController(args[0]));
        server.createContext("/restaurants", new ApiController(args[0]));
        server.createContext("/tables", new ApiController(args[0]));
        server.setExecutor(null);
        server.start();
    }
}

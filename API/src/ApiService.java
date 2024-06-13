import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;

public class ApiService {
    private static ServiceRestaurant.ServiceRestaurant instance;

    public static ServiceRestaurant.ServiceRestaurant getInstance(String address) throws RemoteException {
        if (instance == null) {
            Registry reg = LocateRegistry.getRegistry(address, 1659);

            //Recupere l'interface distante dans l'annuaire de la machine (distant)
            try {
                instance = (ServiceRestaurant.ServiceRestaurant) reg.lookup("restaurants");
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private ApiService() {
    }

    public static String getHazards() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress("www-cache.iutnc.univ-lorraine.fr", 3128)))
                .authenticator(Authenticator.getDefault())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

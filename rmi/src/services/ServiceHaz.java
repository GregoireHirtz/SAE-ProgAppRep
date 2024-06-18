package services;

import services.interfaces.ServiceHazards;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.time.Duration;

public class ServiceHaz extends RemoteServer implements ServiceHazards {
    public String getHazards() throws RemoteException, IOException, InterruptedException {
        try {
            System.out.println(getClientHost() + ": getHazards");
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress("www-cache.iutnc.univ-lorraine.fr", 3128)))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Méthode pour ping le service
     * @return l'ip du client
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException S
     */
    public String ping() throws RemoteException, ServerNotActiveException {
        return getClientHost();
    }
}

import com.sun.net.httpserver.HttpHandler;

import java.rmi.RemoteException;
import java.sql.Date;

public class ApiService {
    public static String getRestaurant(int restaurantId) {
        switch (restaurantId) {
            case 1 -> {
                return "{\"numrestau\":1,\"nom\":\"Le Petit Parisien\",\"latitude\":48.8566,\"longitude\":2.3522}";
            }
            case 2 -> {
                return "{\"numrestau\":2,\"nom\":\"The London Pub\",\"latitude\":51.5074,\"longitude\":-0.1278}";
            }
            case 3 -> {
                return "{\"numrestau\":3,\"nom\":\"La Bella Italia\",\"latitude\":41.9028,\"longitude\":12.4964}";
            }
            case 4 -> {
                return "{\"numrestau\":4,\"nom\":\"Tokyo Sushi House\",\"latitude\":35.6895,\"longitude\":139.6917}";
            }
            default -> {
                return "{\"error\":\"Invalid restaurantId\"}";
            }
        }
    }

    public static String getRestaurants() {
        return "[{\"numrestau\":1,\"nom\":\"Le Petit Parisien\",\"latitude\":48.8566,\"longitude\":2.3522},{\"numrestau\":2,\"nom\":\"The London Pub\",\"latitude\":51.5074,\"longitude\":-0.1278},{\"numrestau\":3,\"nom\":\"La Bella Italia\",\"latitude\":41.9028,\"longitude\":12.4964},{\"numrestau\":4,\"nom\":\"Tokyo Sushi House\",\"latitude\":35.6895,\"longitude\":139.6917}]";
    }

    public static void reserverTable(String nom, String prenom, int nbpers, String telephone, int numrestau, Date date) throws RemoteException, RuntimeException {
        // TODO
    }
}

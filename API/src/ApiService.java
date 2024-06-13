import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApiService {
    private static ServiceRestaurant instance;

    public static ServiceRestaurant getInstance(String address) throws RemoteException {
        if (instance == null) {
            Registry reg = LocateRegistry.getRegistry(address, 1099);

            //Recupere l'interface distante dans l'annuaire de la machine (distant)
            try {
                instance = (ServiceRestaurant) reg.lookup("restaurant");
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private ApiService() {
    }
}

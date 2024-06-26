

import services.interfaces.ServiceRestaurant;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServiceRestaurant {
    private static ServiceRestaurant instance;

    public static ServiceRestaurant getInstance(String address) throws RemoteException {
        if (instance == null) {
            Registry reg = LocateRegistry.getRegistry(address, 1659);

            //Recupere l'interface distante dans l'annuaire de la machine (distant)
            try {
                instance = (ServiceRestaurant) reg.lookup("restaurants");
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private RmiServiceRestaurant() {
    }
}

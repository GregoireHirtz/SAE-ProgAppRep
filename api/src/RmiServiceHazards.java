import services.interfaces.ServiceHazards;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServiceHazards {
    private static ServiceHazards instance;

    public static ServiceHazards getInstance(String address) throws RemoteException {
        if (instance == null) {
            Registry reg = LocateRegistry.getRegistry(address, 1659);

            //Recupere l'interface distante dans l'annuaire de la machine (distant)
            try {
                instance = (ServiceHazards) reg.lookup("hazards");
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private RmiServiceHazards() {
    }
}

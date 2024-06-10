package ServiceRestaurant;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceRestaurant extends Remote {

   String getRestaurants() throws RemoteException, RuntimeException;
   void reserverTable(String nom, String prenom, int nbpers, String telephone, int numrestau) throws RemoteException, RuntimeException;
}

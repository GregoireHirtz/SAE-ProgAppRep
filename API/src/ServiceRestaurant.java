import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface ServiceRestaurant extends Remote {

   String getRestaurants() throws RemoteException, RuntimeException;
   String getRestaurant(int index) throws RemoteException, RuntimeException;
   void reserverTable(String nom, String prenom, int nbpers, String telephone, int numrestau, Date date) throws RemoteException, RuntimeException;
}
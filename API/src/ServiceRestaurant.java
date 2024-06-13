import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface ServiceRestaurant extends Remote {

   String getRestaurants() throws RemoteException, RuntimeException;
   String getRestaurant(int index) throws RemoteException, RuntimeException;
   String getMenuRestaurant(int indexRestaurant) throws RemoteException, RuntimeException;
   String getTablesRestaurant(int indexRestaurant) throws RemoteException, RuntimeException;
   String getTablesLibreRestaurant(int indexRestaurant, Date date) throws RemoteException, RuntimeException;
   String bloquerTable(int indexRestaurant, Date date, int nbPersonnes) throws RemoteException, RuntimeException;
   void reserverTable(String nom, String prenom, String telephone, String ticket) throws RemoteException, RuntimeException;

}
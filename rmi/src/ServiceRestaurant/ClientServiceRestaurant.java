package ServiceRestaurant;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;
import java.util.Calendar;

public class ClientServiceRestaurant {
    public static void main (String args[]) throws RemoteException, NotBoundException {
        ClientServiceRestaurant client = new ClientServiceRestaurant();

        Registry reg = LocateRegistry.getRegistry(args[0]);

        try{
            String[] registerEntries = reg.list();

            System.out.println("--Annuaire--");
            for(String entry : registerEntries) {
                System.out.println(entry);
            }
            System.out.println("--Annuaire--");
        }
        catch(ConnectException e) {
            System.out.println("Annuaire introuvable");
        }

        //Recupere l'interface distante dans l'annuaire de la machine (distant)
        ServiceRestaurant serveur = (ServiceRestaurant) reg.lookup("restaurant");

        // Création d'une date menant à 10 jours plus tard
        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date newDate = new Date(cal.getTimeInMillis());

        //Utilisation du service
        serveur.getRestaurants();
    }
}

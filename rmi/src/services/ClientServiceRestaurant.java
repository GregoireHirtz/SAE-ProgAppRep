package services;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;

public class ClientServiceRestaurant {
    public static void main (String args[]) throws RemoteException, NotBoundException {
        ClientServiceRestaurant client = new ClientServiceRestaurant();

        Registry reg = LocateRegistry.getRegistry(args[0], 1659);

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
        ServiceRestaurant resto = (ServiceRestaurant) reg.lookup("restaurants");

        System.out.println(resto.getRestaurants());
        System.out.println(resto.getRestaurant(1));
        System.out.println(resto.getMenuRestaurant(1));
        System.out.println(resto.getTablesRestaurant(1));
        System.out.println(resto.getTablesLibreRestaurant(1, Date.valueOf("2024-06-15")));
       // String reservation = resto.bloquerTable(1, Date.valueOf("2024-06-15"), 6);
        //System.out.println(reservation);
       // reservation = resto.bloquerTable(1, Date.valueOf("2024-06-15"), 4);
        //System.out.println(reservation);
        //resto.reserverTable("Naigeon", "Adrien", "3630", reservation);
    }
}

package services;

import activeRecord.Reservation;
import bd.Bd;

import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LancerServices {
    static int port = 1659;
    static final String nomServiceRestaurant = "restaurants";
    static final String nomServiceHazards = "hazards";
    static final String url = "jdbc:mariadb://webetu.iutnc.univ-lorraine.fr:3306/hirtz44u";


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main (String args[]) throws RemoteException, UnknownHostException, SQLException, InterruptedException, ServerNotActiveException, NoSuchAlgorithmException {

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Connexion à la base de donnée :");
        System.out.print("-Utilisateur: ");
        String user = "hirtz44u"; //sc.nextLine();
        System.out.print("-Mot de passe: ");
        String password = "password"; //sc.nextLine();

        //---Connexion à la base de donnée
        Bd bd = new Bd(url, user, password);

        //---Création des objets
        ServiceResto serviceResto = new ServiceResto(bd);
        ServiceHaz serviceHaz = new ServiceHaz();

        //crée une interface exportable et l'assigne à un port automatique (0)
        ServiceRestaurant serviceRestaurant = (ServiceRestaurant) UnicastRemoteObject.exportObject(serviceResto, 0);
        ServiceHazards serviceHazards = (ServiceHazards) UnicastRemoteObject.exportObject(serviceHaz, 0);

        //---Récupération de l'annuaire
        Registry reg = LocateRegistry.getRegistry(port);
        try{
            reg.list();
        }
        catch(ConnectException e) {
            reg = LocateRegistry.createRegistry(port);
            System.out.println("Création d'un nouvelle annuaire port [" + ANSI_CYAN + port + ANSI_RESET +"]");
        }

        //Nettoyage de possible relicats
        Reservation.nettoyerTickets(bd);

        //ajout des services a l'annuaire
        reg.rebind(nomServiceRestaurant, serviceRestaurant);
        System.out.println("Services créé, IP:" +  ANSI_CYAN + serviceRestaurant.ping() + ANSI_RESET + " PORT:" + ANSI_CYAN + port + ANSI_RESET + " NOM:" + ANSI_CYAN + nomServiceRestaurant + ANSI_RESET);

        reg.rebind(nomServiceHazards, serviceHaz);
        System.out.println("Services créé, IP:" +  ANSI_CYAN + serviceHazards.ping() + ANSI_RESET + " PORT:" + ANSI_CYAN + port + ANSI_RESET + " NOM:" + ANSI_CYAN + nomServiceHazards + ANSI_RESET);

        //Toutes les minutes, vérifie la validité des tickets
        while (true) {
            TimeUnit.MINUTES.sleep(1);
            Reservation.nettoyerTickets(bd);
        }
    }
}

package ServiceRestaurant;

import activeRecord.Reservation;
import bd.Bd;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LancerRestaurant {
    static int port = 1099;
    static String nomService = "restaurants";
    static String url = "jdbc:mariadb://localhost:3306/miaam";


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main (String args[]) throws RemoteException, UnknownHostException, SQLException, InterruptedException {

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            nomService = args[1];
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Connexion à la base de donnée :");
        System.out.print("-Utilisateur: ");
        String user = sc.nextLine();
        System.out.print("-Mot de passe: ");
        String password = sc.nextLine();

        //---Connexion à la base de donnée
        Bd bd = new Bd(url, user, password);

        //---Création des objets
        ServiceResto serviceResto = new ServiceResto(bd);

        //crée une interface exportable et l'assigne à un port automatique (0)
        ServiceRestaurant service = (ServiceRestaurant) UnicastRemoteObject.exportObject(serviceResto, 0);

        //---Enregistrement dans l'annuaire
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

        //ajout du service a l'annuaire
        reg.rebind(nomService, service);
        System.out.println("Service créé, IP:" +  ANSI_CYAN + InetAddress.getLocalHost() + ANSI_RESET + " PORT:" + ANSI_CYAN + port + ANSI_RESET + " NOM:" + ANSI_CYAN + nomService + ANSI_RESET);

        //Toutes les minutes, vérifie la validité des tickets
        while (true) {
            TimeUnit.MINUTES.sleep(1);
            Reservation.nettoyerTickets(bd);
        }
    }
}

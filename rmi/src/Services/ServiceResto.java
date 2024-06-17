package Services;

import activeRecord.Reservation;
import activeRecord.Restaurant;
import activeRecord.Tabl;
import bd.Bd;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.security.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static Services.utils.Encryption_and_Json.*;

public class ServiceResto extends RemoteServer implements ServiceRestaurant {
    Bd bd;
    private ArrayList<Restaurant> restaurants;
    private HashMap<Integer, Restaurant> restaurantHashMap;
    private final transient KeyPair keys;

    /**
     * Constructeur du service
     * @param bd la base de donnée sur laquelle le service va effectuer ses requêtes
     */
    ServiceResto(Bd bd) throws NoSuchAlgorithmException {
        this.bd = bd;

        // Une nouvelle paire RSA est générée à chaque nouveau service
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        this.keys = generator.generateKeyPair();
    }

    /**
     * Méthode pour récupérer tous les restaurants disponibles dans la bd
     * @return Un string Json contenant tous les objets Restaurants
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour n'importe quel erreur liée au code
     */
    public String getRestaurants() throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": getRestaurants");
            if(bd.haveUpdate("restaurant")) {
                this.restaurants = Restaurant.getAll(bd);

                // On sauvegarde les résultats dans une hashmap
                // Pour que les méthodes utilisant 1 restaurant
                // Soient rapides
                this.restaurantHashMap = new HashMap<>();
                for (Restaurant restaurant : restaurants) {
                    restaurantHashMap.put(restaurant.getNumrestau(), restaurant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving data, DB might be down");
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        return getJson(this.restaurants);
    }

    /**
     * Méthode pour récupérer un restaurant en particulier à partir d'un index
     * @param indexRestaurant index du restaurant
     * @return Un string Json contenant l'objet restaurant
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String getRestaurant(int indexRestaurant) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": getRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        if(indexRestaurant <= 0) {
            throw new RuntimeException("indexRestaurant <= 0");
        }

        getRestaurants(); // Mise à jour des données locales

        return getJson(restaurantHashMap.get(indexRestaurant));
    }

    /**
     * Méthode pour récupérer tous les plats d'un restaurant
     * @param indexRestaurant l'index du restaurant pour lequel on veut récupérer les plats
     * @return Un string Json contenant tous les objets plat
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String getMenuRestaurant(int indexRestaurant) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": getMenuRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        if(indexRestaurant <= 0) {
            throw new RuntimeException("indexRestaurant <= 0");
        }

        getRestaurants(); // Mise à jour des données locales

        try {
            return getJson(restaurantHashMap.get(indexRestaurant).getMenu(bd));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving data, DB might be down");
        }
    }

    /**
     * Méthode pour récupérer toutes les tables d'un restaurant
     * @param indexRestaurant l'index du restaurant pour lequel on veut récupérer les tables
     * @return Un string Json contenant tous les objets Tables
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pout toute autre erreur liée au code
     */
    public String getTablesRestaurant(int indexRestaurant) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": getTablesRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }


        if(indexRestaurant <= 0) {
            throw new RuntimeException("indexRestaurant <= 0");
        }

        getRestaurants(); // Mise à jour des données locales

        try {
            return getJson(restaurantHashMap.get(indexRestaurant).getTables(bd));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving data, DB might be down");
        }
    }

    /**
     * Méthode pour récupérer toutes les tables libres d'un restaurant à un moment donné
     * @param indexRestaurant l'index du restaurant pour lequel on veut récupérer les tables
     * @param date La date à laquelle on veut voir les tables disponibles
     * @return Un string Json contenant tous les objets tables
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String getTablesLibreRestaurant(int indexRestaurant, Date date) throws RemoteException, RuntimeException{
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": getTablesLibreRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        if(indexRestaurant <= 0) {
            throw new RuntimeException("indexRestaurant <= 0");
        }

        getRestaurants(); // Mise à jour des données locales

        try {
            return getJson(restaurantHashMap.get(indexRestaurant).getTablesLibre(bd, date));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving data, DB might be down");
        }
    }

    /**
     * Méthode pour bloquer une table et empêcher les autres utilisateurs de la réserver
     * @param indexRestaurant Index du restaurant où se trouve la table
     * @param date Date à laquelle on veut réserver la table
     * @param nbPersonnes Nombre de personnes dans la réservation
     * @return Un ticket faisant référence à la future réservation. Doit être utilisé pour effectuer une réservation. Retourne une chaîne vide si aucune table n'est disponible
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String bloquerTable(int indexRestaurant, Date date, int nbPersonnes) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": bloquerTable indexRestaurant:" + indexRestaurant + " date:" + date + " nbPersonnes:" + nbPersonnes);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        if(indexRestaurant <= 0) {
            throw new RuntimeException("indexRestaurant <= 0");
        }

        {
            long millis = System.currentTimeMillis();
            Date today = new Date(millis);

            if(!date.after(today)) {
                throw new RuntimeException("given date("+date+") not in the future");
            }
        }

        if(nbPersonnes <= 0) {
            throw new RuntimeException("nbPersonnes <= 0");
        }

        try {
            bd.lockTables("reservation", "restaurant", "tabl");

            // Récupération des tables libres via notre propre méthode
            String json_tablesLibre = getTablesLibreRestaurant(indexRestaurant, date);
            Tabl[] tables = objectMapper.readValue(json_tablesLibre, Tabl[].class);

            // Recherche d'une table libre correspondant aux critères de recherches
            for(Tabl table : tables) {
                if(table.getNbplace() >= nbPersonnes) {
                    Reservation reservation = new Reservation("", "", nbPersonnes, "", indexRestaurant, date, new Date(System.currentTimeMillis()), table.getNumtab());
                    reservation.save(bd);
                    bd.unlockTable();
                    // Chiffrement du ticket
                    return encryptMessage(keys.getPublic(), getJson(reservation));
                }
            }
            return "";

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Data reading error");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error, a parameter might not exist");
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                // Il faut débloquer la table dans toutes les situations
                bd.unlockTable();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Database error");
            }
        }

    }

    /**
     * Méthode pour réserver une table, nécessite un ticket obtenu via bloquerTable()
     * @param nom Nom de l'utilisateur voulant réserver
     * @param prenom Prénom de l'utilisateur voulant réserver
     * @param telephone Numéro de téléphone de l'utilisateur voulant réserver
     * @param ticket Le ticket obtenu via la méthode bloquerTable()
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code, notamment pour un ticket invalide
     */
    public void reserverTable(String nom, String prenom, String telephone, String ticket) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerServices.ANSI_CYAN + getClientHost() + LancerServices.ANSI_RESET + ": reserverTable personne:" + nom + " " + prenom + " telephone:" + telephone + " ticket:" + ticket);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        // Avant toutes choses, déchiffrer le ticket
        try {
            ticket = decryptMessage(keys.getPrivate(), ticket);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

        if(ticket.isEmpty()) {
            throw new RuntimeException("Empty ticket");
        }

        if(nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty()) {
            throw new RuntimeException("Nom("+nom+") Prénom("+prenom+") or téléphone("+telephone+") empty");
        }

        try {
            Reservation reservation = objectMapper.readValue(ticket, Reservation.class);

            // Seuls les bloquages disposent d'une date d'ajout
            if(reservation.getDateajout() == null) {
                throw new RuntimeException("Invalid ticket");
            }

            try {
                boolean invalid = true;
                // On cherche si le ticket correspond à une entrée pour éviter des ajouts non controller
                ArrayList<Reservation> reservations = Reservation.getAll(bd);
                for(Reservation myRes : reservations) {
                    if(myRes.equals(reservation)) {
                        invalid = false;
                    }
                }
                if(invalid) {
                    throw new RuntimeException("Invalid ticket");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while getting database data");
            }

            // ticket valide → On le transforme en réservation définitive
            reservation.setDateajout(null);
            reservation.setNom(nom);
            reservation.setPrenom(prenom);
            reservation.setTelephone(telephone);
            reservation.save(bd);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Ticket error, have you sent the right ticket ? Ticket: " + ticket);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error, are you using an out-of-date ticket ?");
        }
    }

    /**
     * Méthode pour ping le service
     * @return l'ip du client
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException S
     */
    public String ping() throws RemoteException, ServerNotActiveException {
        return getClientHost();
    }

    /** --------------------------------------------------------------------------------------------
     * Méthode main pour visualiser les valeurs de retour des méthodes
     * @param args pas d'arguments requis
     * @throws SQLException Erreur SQL
     * @throws RemoteException Erreur du service
     */
    public static void main (String[] args) throws SQLException, RemoteException, NoSuchAlgorithmException {
        String url = "jdbc:mariadb://localhost:3306/miaam";
        Scanner sc = new Scanner(System.in);
        System.out.println("Connexion à la base de donnée :");
        System.out.print("-Utilisateur: ");
        String user = sc.nextLine();
        System.out.print("-Mot de passe: ");
        String password = sc.nextLine();

        //---Connexion à la base de donnée
        Bd bd = new Bd(url, user, password);

        ServiceResto resto = new ServiceResto(bd);
        System.out.println(resto.getRestaurants());
        System.out.println(resto.getRestaurant(1));
        System.out.println(resto.getMenuRestaurant(1));
        System.out.println(resto.getTablesRestaurant(1));
        System.out.println(resto.getTablesLibreRestaurant(1, Date.valueOf("2024-06-15")));
        String reservation = resto.bloquerTable(1, Date.valueOf("2024-06-15"), 6);
        System.out.println(reservation);
        reservation = resto.bloquerTable(1, Date.valueOf("2024-06-15"), 4);
        System.out.println(reservation);
        resto.reserverTable("Naigeon", "Adrien", "3630", reservation);
    }
}

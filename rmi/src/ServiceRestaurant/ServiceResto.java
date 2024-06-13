package ServiceRestaurant;

import activeRecord.Reservation;
import activeRecord.Restaurant;
import activeRecord.Tabl;
import bd.Bd;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.SqlDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServiceResto extends RemoteServer implements ServiceRestaurant {

    // ----------- static
    public static ObjectMapper objectMapper = new ObjectMapper();

    static { // modification de la mise en forme des dates dans le Json
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new SqlDateSerializer());
        objectMapper.registerModule(module);
    }

    // ----------- attributes

    Bd bd;
    private ArrayList<Restaurant> restaurants;
    private HashMap<Integer, Restaurant> restaurantHashMap;

    /**
     * Constructeur du service
     * @param bd
     */
    ServiceResto(Bd bd) {
        this.bd = bd;
    }

    /**
     * Méthode pour récupérer tous les restaurants disponibles dans la bd
     * @return Un string Json contenant tous les objets Restaurants
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour n'importe quel erreur liée au code
     */
    public String getRestaurants() throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": getRestaurants");
            if(bd.haveUpdate("restaurant")) {
                this.restaurants = Restaurant.getAll(bd);
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
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": getRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }
        getRestaurants();

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
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": getMenuRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        getRestaurants();

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
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": getTablesRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        getRestaurants();

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
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": getTablesLibreRestaurant " + indexRestaurant);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        getRestaurants();

        try {
            return getJson(restaurantHashMap.get(indexRestaurant).getTablesLibre(bd, date));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while retrieving data, DB might be down");
        }
    }

    /**
     * Méthode pour bloquer une table et empêcher une autre personne de la réserver
     * @param indexRestaurant Index du restaurant où se trouve la table
     * @param date Date à laquelle on veut réserver la table
     * @param nbPersonnes Nombre de personnes dans la réservation
     * @return Un ticket faisant référence à la future réservation. Doit être utilisé pour effectuer une réservation. Retourne une chaîne vide si aucune table n'est disponible
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String bloquerTable(int indexRestaurant, Date date, int nbPersonnes) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": bloquerTable indexRestaurant:" + indexRestaurant + " date:" + date + " nbPersonnes:" + nbPersonnes);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        try {
            bd.lockTables("reservation", "restaurant", "tabl");
            String json_tablesLibre = getTablesLibreRestaurant(indexRestaurant, date);
            Tabl[] tables = objectMapper.readValue(json_tablesLibre, Tabl[].class);
            for(Tabl table : tables) {
                if(table.getNbplace() >= nbPersonnes) {
                    Reservation reservation = new Reservation("", "", nbPersonnes, "", indexRestaurant, date, new Date(System.currentTimeMillis()), table.getNumtab());
                    reservation.save(bd);
                    bd.unlockTable();
                    return getJson(reservation);
                }
            }
            return "";

        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException("Data reading error");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Data reading error");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * Méthode pour réserver une table, nécessite un ticket obtenu via bloquerTable()
     * @param nom
     * @param prenom
     * @param telephone
     * @param ticket Le ticket obtenu via la méthode bloquerTable()
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code, notamment pour un ticket invalide
     */
    public void reserverTable(String nom, String prenom, String telephone, String ticket) throws RemoteException, RuntimeException {
        try {
            System.out.println(LancerRestaurant.ANSI_CYAN + getClientHost() + LancerRestaurant.ANSI_RESET + ": reserverTable personne:" + nom + " " + prenom + " telephone:" + telephone + " ticket:" + ticket);
        } catch (ServerNotActiveException e) {
            throw new RuntimeException(e);
        }

        try {
            Reservation reservation = objectMapper.readValue(ticket, Reservation.class);
            reservation.setDateajout(null);
            reservation.setNom(nom);
            reservation.setPrenom(prenom);
            reservation.setTelephone(telephone);
            reservation.save(bd);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new RuntimeException("Data reading error, have you sent the right ticket ?");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Data reading error, have you sent the right ticket ?");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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

    /**
     * Méthode pour transformer un objet en string Json, en gérant les erreurs
     * @param object l'objet à transformer en Json
     * @return l'objet sous forme de Json
     * @throws RuntimeException En cas d'erreur pendant la création du Json
     */
    private static String getJson(Object object) throws RuntimeException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while processing JSON");
        }
    }

    /** --------------------------------------------------------------------------------------------
     * Méthode main pour visualiser les valeurs de retour des méthodes
     * @param args pas d'arguments requis
     * @throws SQLException
     * @throws RemoteException
     */
    public static void main (String[] args) throws SQLException, RemoteException {
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

package ServiceRestaurant;

import activeRecord.Reservation;
import activeRecord.Restaurant;
import activeRecord.Tabl;
import bd.Bd;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServiceResto extends RemoteServer implements ServiceRestaurant {

    // ----------- static
    public static ObjectMapper objectMapper = new ObjectMapper();

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
        }

        return getJson(this.restaurants);
    }

    /**
     * Méthode pour récupérer un restaurant en particulier à partir d'un index
     * @param index index du restaurant
     * @return Un string Json contenant l'objet restaurant
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String getRestaurant(int index) throws RemoteException, RuntimeException {
        getRestaurants();

        return getJson(restaurantHashMap.get(index));
    }

    /**
     * Méthode pour récupérer tous les plats d'un restaurant
     * @param indexRestaurant l'index du restaurant pour lequel on veut récupérer les plats
     * @return Un string Json contenant tous les objets plat
     * @throws RemoteException En cas d'erreur RMI
     * @throws RuntimeException Pour toute autre erreur liée au code
     */
    public String getPlatsRestaurant(int indexRestaurant) throws RemoteException, RuntimeException {
        getRestaurants();

        try {
            return getJson(restaurantHashMap.get(indexRestaurant).getPlats(bd));
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
    public String getTableRestaurant(int indexRestaurant) throws RemoteException, RuntimeException {
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
    public String getTableLibreRestaurant(int indexRestaurant, Date date) throws RemoteException, RuntimeException{
        getRestaurants();

        try {
            return getJson(restaurantHashMap.get(indexRestaurant).getTablesLibre(bd, Date date));
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
            bd.lockTable("reservation");
            String json_tablesLibre = getTableLibreRestaurant(indexRestaurant, date);
            Tabl[] tables = objectMapper.readValue(json_tablesLibre, Tabl[].class);
            for(Tabl table : tables) {
                if(table.getNbplace() >= nbPersonnes) {
                    Date dateajout;
                    Reservation reservation = new Reservation("", "", nbPersonnes, "", indexRestaurant, date, dateajout,table.getNumtab());
                    reservation.save(bd);
                    bd.unLockTable("reservation");
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
            Reservation reservation = objectMapper.readValue(ticket, Reservation.class);
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
    }
}
